package br.gov.anvisa.portfolio.domain.vinculo;

/**
 * Tipos de vinculo N:N entre agregados e suas entidades relacionadas.
 *
 * <p>Todos os vinculos possuem a mesma estrutura (dono, vinculado e registro ativo),
 * exceto {@link #SCHEMA}, que tambem possui o indicador de schema principal.
 */
public enum TipoVinculo {

    SCHEMA(true),
    URL(false),
    SOFTWARE(false),
    SERVIDOR(false),
    BASE_DE_DADO(false),
    BACKUP(false);

    private final boolean possuiMarcaPrincipal;

    TipoVinculo(boolean possuiMarcaPrincipal) {
        this.possuiMarcaPrincipal = possuiMarcaPrincipal;
    }

    public boolean possuiMarcaPrincipal() {
        return possuiMarcaPrincipal;
    }
}