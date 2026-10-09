package br.gov.anvisa.portfolio.infrastructure.persistence.vinculo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.anvisa.portfolio.domain.vinculo.TipoVinculo;
import br.gov.anvisa.portfolio.domain.vinculo.VinculoPort;

/**
 * Adaptador JDBC da porta {@link VinculoPort}, responsavel pelas tabelas RL_*.
 *
 * <p>A substituicao de vinculos e feita dentro da transacao da aplicacao, em duas etapas:
 * remocao do conjunto anterior e insercao do novo conjunto.
 */
@Repository
public class VinculoJdbcAdapter implements VinculoPort {

    private static final String COLUNA_ATIVO = "ST_REGISTRO_ATIVO";
    private static final String SIM = "S";
    private static final String NAO = "N";

    private final JdbcTemplate jdbcTemplate;
    private final String esquema;

    public VinculoJdbcAdapter(JdbcTemplate jdbcTemplate,
                              @Value("${portfolio.jdbc.esquema:DBPORTIFOLIO}") String esquema) {
        this.jdbcTemplate = jdbcTemplate;
        this.esquema = esquema;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> listarVinculados(Long donoId, TipoVinculo tipo) {
        VinculoTabela tabela = VinculoTabela.de(tipo);
        return jdbcTemplate.queryForList(
                "SELECT %s FROM %s WHERE %s = ? AND %s = '%s' ORDER BY %s"
                        .formatted(tabela.colunaVinculado(), tabelaQualificada(tabela), tabela.colunaDono(),
                                COLUNA_ATIVO, SIM, tabela.colunaVinculado()),
                Long.class, donoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> listarPrincipais(Long donoId, TipoVinculo tipo) {
        VinculoTabela tabela = VinculoTabela.de(tipo);
        if (tabela.colunaPrincipal() == null) {
            return List.of();
        }
        return jdbcTemplate.queryForList(
                "SELECT %s FROM %s WHERE %s = ? AND %s = '%s' ORDER BY %s"
                        .formatted(tabela.colunaVinculado(), tabelaQualificada(tabela), tabela.colunaDono(),
                                tabela.colunaPrincipal(), SIM, tabela.colunaVinculado()),
                Long.class, donoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> listarDonos(Long vinculadoId, TipoVinculo tipo) {
        VinculoTabela tabela = VinculoTabela.de(tipo);
        return jdbcTemplate.queryForList(
                "SELECT %s FROM %s WHERE %s = ? AND %s = '%s' ORDER BY %s"
                        .formatted(tabela.colunaDono(), tabelaQualificada(tabela), tabela.colunaVinculado(),
                                COLUNA_ATIVO, SIM, tabela.colunaDono()),
                Long.class, vinculadoId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean possuiVinculo(Long donoId, TipoVinculo tipo, Long vinculadoId) {
        VinculoTabela tabela = VinculoTabela.de(tipo);
        Integer quantidade = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM %s.%s WHERE %s = ? AND %s = ? AND %s = '%s'"
                        .formatted(esquema, tabela.tabela(), tabela.colunaDono(), tabela.colunaVinculado(),
                                COLUNA_ATIVO, SIM),
                Integer.class, donoId, vinculadoId);
        return quantidade != null && quantidade > 0;
    }

    @Override
    @Transactional
    public void vincular(Long donoId, TipoVinculo tipo, Collection<Long> vinculadosIds) {
        VinculoTabela tabela = VinculoTabela.de(tipo);
        for (Long vinculadoId : vinculadosIds) {
            if (possuiVinculo(donoId, tipo, vinculadoId)) {
                continue;
            }
            jdbcTemplate.update("INSERT INTO %s (%s, %s, %s) VALUES (?, ?, '%s')"
                    .formatted(tabelaQualificada(tabela), tabela.colunaDono(), tabela.colunaVinculado(),
                            COLUNA_ATIVO, SIM), donoId, vinculadoId);
        }
    }

    @Override
    @Transactional
    public void desvincular(Long donoId, TipoVinculo tipo, Collection<Long> vinculadosIds) {
        VinculoTabela tabela = VinculoTabela.de(tipo);
        if (vinculadosIds.isEmpty()) {
            return;
        }
        List<Object> parametros = new ArrayList<>();
        parametros.add(donoId);
        vinculadosIds.forEach(id -> parametros.add(id));
        String sql = "DELETE FROM %s WHERE %s = ? AND %s IN (%s)"
                .formatted(tabelaQualificada(tabela), tabela.colunaDono(), tabela.colunaVinculado(),
                        placeholders(vinculadosIds.size()));
        jdbcTemplate.update(sql, parametros.toArray());
    }

    @Override
    @Transactional
    public void substituirVinculos(Long donoId, TipoVinculo tipo, Collection<Long> vinculadosIds) {
        removerTodos(donoId, tipo);
        vincular(donoId, tipo, vinculadosIds);
    }

    @Override
    @Transactional
    public void definirPrincipal(Long donoId, TipoVinculo tipo, Long vinculadoId) {
        VinculoTabela tabela = VinculoTabela.de(tipo);
        if (tabela.colunaPrincipal() == null) {
            return;
        }

        jdbcTemplate.update("UPDATE %s SET %s = ? WHERE %s = ?"
                .formatted(tabelaQualificada(tabela), tabela.colunaPrincipal(), tabela.colunaDono()), NAO, donoId);

        if (vinculadoId != null) {
            jdbcTemplate.update("UPDATE %s SET %s = ? WHERE %s = ? AND %s = ?"
                            .formatted(tabelaQualificada(tabela), tabela.colunaPrincipal(), tabela.colunaDono(),
                                    tabela.colunaVinculado()),
                    SIM, donoId, vinculadoId);
        }
    }

    @Override
    @Transactional
    public void removerTodos(Long donoId, TipoVinculo tipo) {
        VinculoTabela tabela = VinculoTabela.de(tipo);
        jdbcTemplate.update("DELETE FROM %s WHERE %s = ?".formatted(tabelaQualificada(tabela), tabela.colunaDono()),
                donoId);
    }

    @Override
    @Transactional
    public void desvincularDonos(Long vinculadoId, TipoVinculo tipo) {
        VinculoTabela tabela = VinculoTabela.de(tipo);
        jdbcTemplate.update("DELETE FROM %s WHERE %s = ?".formatted(tabelaQualificada(tabela),
                tabela.colunaVinculado()), vinculadoId);
    }

    private String tabelaQualificada(VinculoTabela tabela) {
        return "%s.%s".formatted(esquema, tabela.tabela());
    }

    private String placeholders(int quantidade) {
        return String.join(", ", java.util.Collections.nCopies(quantidade, "?"));
    }
}