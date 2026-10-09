package br.gov.anvisa.portfolio.infrastructure.persistence.vinculo;

import br.gov.anvisa.portfolio.domain.vinculo.TipoVinculo;

/**
 * Metadados das tabelas de relacionamento (RL_*).
 *
 * <p>O nome logico {@code CO_SISTEMA} e reutilizado por varias tabelas e por isso recebe
 * o alias logico {@code dono}.
 */
public enum VinculoTabela {

    SCHEMA(TipoVinculo.SCHEMA, "RL_SISTEMA_SCHEMA", "CO_SISTEMA", "CO_SCHEMA", "ST_SCHEMA_PRINCIPAL"),
    URL(TipoVinculo.URL, "RL_SISTEMA_URL", "CO_SISTEMA", "CO_URL", null),
    SOFTWARE(TipoVinculo.SOFTWARE, "RL_SISTEMA_SOFTWARE", "CO_SISTEMA", "CO_SOFTWARE", null),
    SERVIDOR(TipoVinculo.SERVIDOR, "RL_SISTEMA_SERVIDOR", "CO_SISTEMA", "CO_SERVIDOR", null),
    BASE_DE_DADO(TipoVinculo.BASE_DE_DADO, "RL_SERVIDOR_BASEDEDADO", "CO_SERVIDOR", "CO_BASEDEDADOS", null),
    BACKUP(TipoVinculo.BACKUP, "RL_SERVIDOR_BACKUP", "CO_SERVIDOR", "CO_BACKUP", null);

    private final TipoVinculo tipo;
    private final String tabela;
    private final String colunaDono;
    private final String colunaVinculado;
    private final String colunaPrincipal;

    VinculoTabela(TipoVinculo tipo, String tabela, String colunaDono, String colunaVinculado, String colunaPrincipal) {
        this.tipo = tipo;
        this.tabela = tabela;
        this.colunaDono = colunaDono;
        this.colunaVinculado = colunaVinculado;
        this.colunaPrincipal = colunaPrincipal;
    }

    public String tabela() {
        return tabela;
    }

    public String colunaDono() {
        return colunaDono;
    }

    public String colunaVinculado() {
        return colunaVinculado;
    }

    public String colunaPrincipal() {
        return colunaPrincipal;
    }

    public static VinculoTabela de(TipoVinculo tipo) {
        for (VinculoTabela vinculo : values()) {
            if (vinculo.tipo == tipo) {
                return vinculo;
            }
        }
        throw new IllegalArgumentException("Vinculo sem metadados: %s".formatted(tipo));
    }
}