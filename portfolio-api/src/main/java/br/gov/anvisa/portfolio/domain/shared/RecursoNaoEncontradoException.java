package br.gov.anvisa.portfolio.domain.shared;

/**
 * Excecao de dominio para recursos inexistentes. Traduzida para HTTP 404.
 */
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String recurso, Object identificador) {
        super("%s nao encontrado(a): %s".formatted(recurso, identificador));
    }
}