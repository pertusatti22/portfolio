package br.gov.anvisa.portfolio.domain.shared;

/**
 * Excecao de dominio para violacao de unicidade. Traduzida para HTTP 409.
 */
public class RegistroDuplicadoException extends RuntimeException {

    public RegistroDuplicadoException(String mensagem) {
        super(mensagem);
    }
}