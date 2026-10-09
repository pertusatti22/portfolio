package br.gov.anvisa.portfolio.api.common;

import br.gov.anvisa.portfolio.domain.shared.PaginaRequest;

/**
 * Conversao dos parametros de paginacao da API para o modelo de dominio.
 */
public final class Paginas {

    private Paginas() {
    }

    /**
     * @param pagina      pagina zero based
     * @param tamanho     itens por pagina
     * @param ordenacao   coluna de ordenacao (validada na camada de infraestrutura)
     * @param crescente   direcao da ordenacao; quando nula, assume crescente
     */
    public static PaginaRequest requisicao(Integer pagina, Integer tamanho, String ordenacao, Boolean crescente) {
        return new PaginaRequest(pagina == null ? 0 : pagina,
                tamanho == null ? PaginaRequest.TAMANHO_PADRAO : tamanho,
                ordenacao,
                crescente == null || crescente);
    }
}