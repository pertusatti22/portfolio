package br.gov.anvisa.portfolio.domain.shared;

/**
 * Excecao de dominio para regras de negocio violadas.
 */
public class RegraDeNegocioException extends RuntimeException {

    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }
}