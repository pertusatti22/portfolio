package br.gov.anvisa.portfolio.domain.url;

import br.gov.anvisa.portfolio.domain.shared.RegraDeNegocioException;

/**
 * Tipo do ambiente ao qual a URL se refere (TB_URL.TP_URL).
 */
public enum TipoUrl {

    PRODUCAO('P', "Producao"),
    RELEASE('R', "Release"),
    HOMOLOGACAO('H', "Homologacao"),
    DESENVOLVIMENTO('D', "Desenvolvimento");

    private final char codigo;
    private final String rotulo;

    TipoUrl(char codigo, String rotulo) {
        this.codigo = codigo;
        this.rotulo = rotulo;
    }

    public char codigo() {
        return codigo;
    }

    public String rotulo() {
        return rotulo;
    }

    public static TipoUrl doCodigo(char codigo) {
        for (TipoUrl tipo : values()) {
            if (tipo.codigo == Character.toUpperCase(codigo)) {
                return tipo;
            }
        }
        throw new RegraDeNegocioException("Tipo de URL invalido: %s".formatted(codigo));
    }

    public static TipoUrl padrao() {
        return DESENVOLVIMENTO;
    }
}