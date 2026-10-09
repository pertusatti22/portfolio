package br.gov.anvisa.portfolio.api.common;

import java.util.List;
import java.util.function.Function;

import br.gov.anvisa.portfolio.domain.shared.Pagina;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Envelope padronizado de resultados paginados.
 *
 * @param conteudo     itens da pagina
 * @param pagina       numero da pagina (zero based)
 * @param tamanho      quantidade de itens por pagina
 * @param total        quantidade total de itens
 * @param totalPaginas quantidade total de paginas
 */
@Schema(name = "PageResponse", description = "Pagina de resultados")
public record PageResponse<T>(List<T> conteudo,
                              int pagina,
                              int tamanho,
                              long total,
                              int totalPaginas) {

    public static <T> PageResponse<T> de(Pagina<T> pagina) {
        return new PageResponse<>(pagina.itens(),
                pagina.pagina(),
                pagina.tamanho(),
                pagina.totalElementos(),
                pagina.totalPaginas());
    }

    /**
     * Converte os itens da pagina de dominio para itens de resposta.
     */
    public static <D, R> PageResponse<R> de(Pagina<D> pagina, Function<D, R> conversor) {
        return new PageResponse<>(pagina.itens().stream().map(conversor).toList(),
                pagina.pagina(),
                pagina.tamanho(),
                pagina.totalElementos(),
                pagina.totalPaginas());
    }
}