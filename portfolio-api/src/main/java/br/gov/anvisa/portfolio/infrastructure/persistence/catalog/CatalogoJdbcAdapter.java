package br.gov.anvisa.portfolio.infrastructure.persistence.catalog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.anvisa.portfolio.domain.catalogo.Catalogo;
import br.gov.anvisa.portfolio.domain.catalogo.CatalogoPort;
import br.gov.anvisa.portfolio.domain.catalogo.CatalogoTipo;
import br.gov.anvisa.portfolio.domain.shared.Pagina;
import br.gov.anvisa.portfolio.domain.shared.PaginaRequest;

/**
 * Adaptador JDBC da porta {@link CatalogoPort}.
 *
 * <p>Os catalogos possuem estrutura identica e por isso sao manipulados por um unico
 * adaptador com JDBC template, evitando uma entidade JPA para cada tabela TD_*.
 * Os nomes de tabela e coluna vem exclusivamente de {@link CatalogoTabela} (metadados fixos).
 */
@Repository
public class CatalogoJdbcAdapter implements CatalogoPort {

    private static final String COLUNA_ATIVO = "ST_REGISTRO_ATIVO";
    private static final String SIM = "S";
    private static final String NAO = "N";

    private final JdbcTemplate jdbcTemplate;
    private final String esquema;

    public CatalogoJdbcAdapter(JdbcTemplate jdbcTemplate,
                               @Value("${portfolio.jdbc.esquema:DBPORTIFOLIO}") String esquema) {
        this.jdbcTemplate = jdbcTemplate;
        this.esquema = esquema;
    }

    @Override
    @Transactional(readOnly = true)
    public Pagina<Catalogo> listar(CatalogoTipo tipo, String filtro, boolean somenteAtivos, PaginaRequest paginacao) {
        CatalogoTabela tabela = CatalogoTabela.de(tipo);
        List<Object> parametros = new ArrayList<>();
        String where = montarWhere(tabela, filtro, somenteAtivos, parametros);

        Long total = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM %s%s".formatted(tabelaQualificada(tabela), where), Long.class, parametros.toArray());
        long totalElementos = total == null ? 0L : total;

        int deslocamento = paginacao.pagina() * paginacao.tamanho();
        if (deslocamento >= totalElementos) {
            return Pagina.de(List.of(), paginacao.pagina(), paginacao.tamanho(), totalElementos);
        }

        String sql = "SELECT %s FROM %s%s ORDER BY %s OFFSET %d ROWS FETCH NEXT %d ROWS ONLY"
                .formatted(colunasSelecao(tabela), tabelaQualificada(tabela), where, tabela.colunaDescricao(),
                        deslocamento, paginacao.tamanho());
        List<Catalogo> itens = jdbcTemplate.query(sql, mapeador(tabela), parametros.toArray());
        return Pagina.de(itens, paginacao.pagina(), paginacao.tamanho(), totalElementos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Catalogo> listarTodos(CatalogoTipo tipo, boolean somenteAtivos) {
        CatalogoTabela tabela = CatalogoTabela.de(tipo);
        List<Object> parametros = new ArrayList<>();
        String where = montarWhere(tabela, null, somenteAtivos, parametros);
        String sql = "SELECT %s FROM %s%s ORDER BY %s"
                .formatted(colunasSelecao(tabela), tabelaQualificada(tabela), where, tabela.colunaDescricao());
        return jdbcTemplate.query(sql, mapeador(tabela), parametros.toArray());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Catalogo> buscarPorId(CatalogoTipo tipo, Long id) {
        CatalogoTabela tabela = CatalogoTabela.de(tipo);
        String sql = "SELECT %s FROM %s WHERE %s = ?"
                .formatted(colunasSelecao(tabela), tabelaQualificada(tabela), tabela.colunaPk());
        return primeiro(sql, mapeador(tabela), id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Catalogo> buscarPorDescricao(CatalogoTipo tipo, String descricao) {
        CatalogoTabela tabela = CatalogoTabela.de(tipo);
        String sql = "SELECT %s FROM %s WHERE UPPER(%s) = UPPER(?)"
                .formatted(colunasSelecao(tabela), tabelaQualificada(tabela), tabela.colunaDescricao());
        return primeiro(sql, mapeador(tabela), descricao);
    }

    @Override
    @Transactional
    public Catalogo inserir(CatalogoTipo tipo, Catalogo catalogo) {
        CatalogoTabela tabela = CatalogoTabela.de(tipo);
        Long id = proximoValor(tabela.sequence());

        List<String> colunas = new ArrayList<>(List.of(tabela.colunaPk(), tabela.colunaDescricao()));
        List<Object> valores = new ArrayList<>(List.of(id, catalogo.descricao()));

        if (tabela.colunaVersao() != null) {
            colunas.add(tabela.colunaVersao());
            valores.add(catalogo.versao());
        }
        if (tabela.colunaLiberado() != null) {
            colunas.add(tabela.colunaLiberado());
            valores.add(paraIndicador(Boolean.TRUE.equals(catalogo.liberado())));
        }
        colunas.add(COLUNA_ATIVO);
        valores.add(paraIndicador(catalogo.registroAtivo()));

        String sql = "INSERT INTO %s (%s) VALUES (%s)"
                .formatted(tabelaQualificada(tabela),
                        String.join(", ", colunas),
                        String.join(", ", colunas.stream().map(coluna -> "?").toList()));
        jdbcTemplate.update(sql, valores.toArray());

        return catalogo.comId(id);
    }

    @Override
    @Transactional
    public Catalogo alterar(CatalogoTipo tipo, Catalogo catalogo) {
        CatalogoTabela tabela = CatalogoTabela.de(tipo);
        List<String> atribuicoes = new ArrayList<>(List.of(tabela.colunaDescricao() + " = ?"));
        List<Object> parametros = new ArrayList<>(List.of(catalogo.descricao()));

        if (tabela.colunaVersao() != null) {
            atribuicoes.add(tabela.colunaVersao() + " = ?");
            parametros.add(catalogo.versao());
        }
        if (tabela.colunaLiberado() != null) {
            atribuicoes.add(tabela.colunaLiberado() + " = ?");
            parametros.add(paraIndicador(Boolean.TRUE.equals(catalogo.liberado())));
        }
        atribuicoes.add(COLUNA_ATIVO + " = ?");
        parametros.add(paraIndicador(catalogo.registroAtivo()));
        parametros.add(catalogo.id());

        String sql = "UPDATE %s SET %s WHERE %s = ?"
                .formatted(tabelaQualificada(tabela), String.join(", ", atribuicoes), tabela.colunaPk());
        jdbcTemplate.update(sql, parametros.toArray());

        return catalogo;
    }

    @Override
    @Transactional
    public void desativar(CatalogoTipo tipo, Long id) {
        CatalogoTabela tabela = CatalogoTabela.de(tipo);
        jdbcTemplate.update("UPDATE %s SET %s = ? WHERE %s = ?"
                .formatted(tabelaQualificada(tabela), COLUNA_ATIVO, tabela.colunaPk()), NAO, id);
    }

    @Override
    @Transactional
    public void excluir(CatalogoTipo tipo, Long id) {
        CatalogoTabela tabela = CatalogoTabela.de(tipo);
        jdbcTemplate.update("DELETE FROM %s WHERE %s = ?".formatted(tabelaQualificada(tabela), tabela.colunaPk()), id);
    }

    private Optional<Catalogo> primeiro(String sql, RowMapper<Catalogo> mapeador, Object parametro) {
        return jdbcTemplate.query(sql, mapeador, parametro).stream().findFirst();
    }

    private String montarWhere(CatalogoTabela tabela, String filtro, boolean somenteAtivos, List<Object> parametros) {
        List<String> condicoes = new ArrayList<>();
        if (filtro != null && !filtro.isBlank()) {
            condicoes.add("UPPER(%s) LIKE UPPER(?)".formatted(tabela.colunaDescricao()));
            parametros.add("%%%s%%".formatted(filtro.trim()));
        }
        if (somenteAtivos) {
            condicoes.add("%s = '%s'".formatted(COLUNA_ATIVO, SIM));
        }
        return condicoes.isEmpty() ? "" : " WHERE " + String.join(" AND ", condicoes);
    }

    private String colunasSelecao(CatalogoTabela tabela) {
        StringBuilder colunas = new StringBuilder()
                .append(tabela.colunaPk()).append(", ")
                .append(tabela.colunaDescricao()).append(", ")
                .append(COLUNA_ATIVO);
        if (tabela.colunaVersao() != null) {
            colunas.append(", ").append(tabela.colunaVersao());
        }
        if (tabela.colunaLiberado() != null) {
            colunas.append(", ").append(tabela.colunaLiberado());
        }
        return colunas.toString();
    }

    private RowMapper<Catalogo> mapeador(CatalogoTabela tabela) {
        return (resultado, indice) -> new Catalogo(resultado.getLong(tabela.colunaPk()),
                resultado.getString(tabela.colunaDescricao()),
                tabela.colunaVersao() == null ? null : resultado.getString(tabela.colunaVersao()),
                tabela.colunaLiberado() == null ? null : paraBooleano(resultado.getString(tabela.colunaLiberado())),
                paraBooleano(resultado.getString(COLUNA_ATIVO)));
    }

    private String tabelaQualificada(CatalogoTabela tabela) {
        return "%s.%s".formatted(esquema, tabela.tabela());
    }

    private Long proximoValor(String sequence) {
        return jdbcTemplate.queryForObject("SELECT %s.%s.NEXTVAL FROM DUAL".formatted(esquema, sequence), Long.class);
    }

    private String paraIndicador(boolean valor) {
        return valor ? SIM : NAO;
    }

    private Boolean paraBooleano(String valor) {
        return valor != null && SIM.equalsIgnoreCase(valor.trim());
    }
}