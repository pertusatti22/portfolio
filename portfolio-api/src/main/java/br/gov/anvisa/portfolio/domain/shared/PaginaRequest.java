package br.gov.anvisa.portfolio.domain.shared;

/**
 * Parametros de paginacao e ordenacao independentes do framework.
 *
 * @param pagina     numero da pagina (zero based)
 * @param tamanho    quantidade maxima de itens por pagina
 * @param ordenacao  coluna de ordenacao
 * @param crescente  direcao da ordenacao
 */
public record PaginaRequest(int pagina, int tamanho, String ordenacao, boolean crescente) {

    public static final int TAMANHO_PADRAO = 20;
    public static final int TAMANHO_MAXIMO = 200;

    public PaginaRequest {
        if (pagina < 0) {
            pagina = 0;
        }
        if (tamanho <= 0) {
            tamanho = TAMANHO_PADRAO;
        }
        if (tamanho > TAMANHO_MAXIMO) {
            tamanho = TAMANHO_MAXIMO;
        }
    }

    public static PaginaRequest padrao() {
        return new PaginaRequest(0, TAMANHO_PADRAO, null, true);
    }

    public static PaginaRequest padrao(String ordenacao) {
        return new PaginaRequest(0, TAMANHO_PADRAO, ordenacao, true);
    }
}