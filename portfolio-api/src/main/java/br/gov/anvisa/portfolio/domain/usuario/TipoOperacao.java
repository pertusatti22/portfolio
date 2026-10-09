package br.gov.anvisa.portfolio.domain.usuario;

import br.gov.anvisa.portfolio.domain.shared.RegraDeNegocioException;

/**
 * Tipo de operacao registrada no historico de usuarios (TB_HIST_USUARIO.TP_OPERACAO).
 */
public enum TipoOperacao {

    INSERCAO('I'),
    ALTERACAO('U'),
    EXCLUSAO('D');

    private final char codigo;

    TipoOperacao(char codigo) {
        this.codigo = codigo;
    }

    public char codigo() {
        return codigo;
    }

    public static TipoOperacao doCodigo(char codigo) {
        for (TipoOperacao operacao : values()) {
            if (operacao.codigo == Character.toUpperCase(codigo)) {
                return operacao;
            }
        }
        throw new RegraDeNegocioException("Operacao invalida: %s".formatted(codigo));
    }
}