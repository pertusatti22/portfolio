package br.gov.anvisa.portfolio.infrastructure.persistence.adapter;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.anvisa.portfolio.domain.painel.PainelPort;
import br.gov.anvisa.portfolio.domain.url.TipoUrl;

/**
 * Adaptador JDBC da porta {@link PainelPort}, responsavel pelos indicadores do painel.
 */
@Repository
public class PainelJdbcAdapter implements PainelPort {

    private final JdbcTemplate jdbcTemplate;
    private final String esquema;

    public PainelJdbcAdapter(JdbcTemplate jdbcTemplate,
                             @Value("${portfolio.jdbc.esquema:DBPORTIFOLIO}") String esquema) {
        this.jdbcTemplate = jdbcTemplate;
        this.esquema = esquema;
    }

    @Override
    @Transactional(readOnly = true)
    public ResumoPortfolio montarResumo() {
        long totalSistemas = contar("TB_SISTEMA", null);
        long sistemasAtivos = contar("TB_SISTEMA", "ST_REGISTRO_ATIVO = 'S'");
        long sistemasNaoValidados = contar("TB_SISTEMA", "ST_VALIDADO = 'N'");
        long totalServidores = contar("TB_SERVIDOR", null);
        long servidoresLigados = contar("TB_SERVIDOR", "ST_LIGADO = 'S'");
        long totalUrls = contar("TB_URL", "ST_REGISTRO_ATIVO = 'S'");
        long totalRotinas = contar("TB_ROTINA", "ST_REGISTRO_ATIVO = 'S'");
        long totalBackups = contar("TB_BACKUP", null);
        long totalUsuarios = contar("TB_USUARIO", null);
        long usuariosInativos = contar("TB_USUARIO", "DT_FIM_VIGENCIA IS NOT NULL AND DT_FIM_VIGENCIA < TRUNC(SYSDATE)");

        return new ResumoPortfolio(totalSistemas,
                sistemasAtivos,
                sistemasNaoValidados,
                totalServidores,
                servidoresLigados,
                totalUrls,
                totalRotinas,
                totalBackups,
                totalUsuarios,
                usuariosInativos,
                sistemasPorOrigem(),
                sistemasPorBaseDeDado(),
                urlsPorTipo());
    }

    private List<ItemContagem> sistemasPorOrigem() {
        return consultarContagem("""
                SELECT NVL(o.NO_ORIGEM, 'Sem origem') AS ROTULO, COUNT(s.CO_SEQ_SISTEMA) AS TOTAL
                FROM %s.TB_SISTEMA s
                LEFT JOIN %s.TD_ORIGEM o ON o.CO_SEQ_ORIGEM = s.CO_ORIGEM
                WHERE s.ST_REGISTRO_ATIVO = 'S'
                GROUP BY NVL(o.NO_ORIGEM, 'Sem origem')
                ORDER BY TOTAL DESC
                """.formatted(esquema, esquema));
    }

    private List<ItemContagem> sistemasPorBaseDeDado() {
        return consultarContagem("""
                SELECT NVL(b.NO_BASEDEDADO, 'Sem base de dados') AS ROTULO, COUNT(s.CO_SEQ_SISTEMA) AS TOTAL
                FROM %s.TB_SISTEMA s
                LEFT JOIN %s.TD_BASEDEDADO b ON b.CO_SEQ_BASEDEDADO = s.CO_BASEDEDADOS
                WHERE s.ST_REGISTRO_ATIVO = 'S'
                GROUP BY NVL(b.NO_BASEDEDADO, 'Sem base de dados')
                ORDER BY TOTAL DESC
                """.formatted(esquema, esquema));
    }

    private List<ItemContagem> urlsPorTipo() {
        List<ItemContagem> itens = consultarContagem("""
                SELECT TP_URL AS ROTULO, COUNT(CO_SEQ_URL) AS TOTAL
                FROM %s.TB_URL
                WHERE ST_REGISTRO_ATIVO = 'S'
                GROUP BY TP_URL
                """.formatted(esquema));
        return itens.stream()
                .map(item -> new ItemContagem(rotuloTipo(item.rotulo()), item.total()))
                .toList();
    }

    private String rotuloTipo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            return "Nao informado";
        }
        return TipoUrl.doCodigo(codigo.charAt(0)).rotulo();
    }

    private long contar(String tabela, String condicao) {
        String sql = "SELECT COUNT(*) FROM %s.%s%s".formatted(esquema, tabela,
                condicao == null ? "" : " WHERE " + condicao);
        Long total = jdbcTemplate.queryForObject(sql, Long.class);
        return total == null ? 0L : total;
    }

    private List<ItemContagem> consultarContagem(String sql) {
        return jdbcTemplate.query(sql, (resultado, indice) ->
                new ItemContagem(resultado.getString("ROTULO"), resultado.getLong("TOTAL")));
    }
}