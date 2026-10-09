package br.gov.anvisa.portfolio.domain.shared;

import java.util.List;
import java.util.function.Function;

/**
 * Pagina de resultados devolvida pelos repositorios (portas) de dominio.
 *
 * @param itens       conteudo da pagina
 * @param pagina      numero da pagina (zero based)
 * @param tamanho     tamanho da pagina
 * @param totalElementos quantidade total de elementos do filtro
 * @param totalPaginas   quantidade total de paginas
 * @param <T>         tipo do item
 */
public record Pagina<T>(List<T> itens, int pagina, int tamanho, long totalElementos, int totalPaginas) {

    public Pagina {
        itens = List.copyOf(itens);
    }

    public static <T> Pagina<T> de(List<T> itens, int pagina, int tamanho, long totalElementos) {
        int totalPaginas = tamanho <= 0 ? 0 : (int) Math.ceil((double) totalElementos / tamanho);
        return new Pagina<>(itens, pagina, tamanho, totalElementos, totalPaginas);
    }

    public static <T> Pagina<T> unica(List<T> itens) {
        List<T> copia = List.copyOf(itens);
        return new Pagina<>(copia, 0, Math.max(copia.size(), 1), copia.size(), copia.isEmpty() ? 0 : 1);
    }

    public <R> Pagina<R> map(Function<? super T, ? extends R> conversor) {
        List<R> convertidos = itens.stream().map(conversor).collect(java.util.stream.Collectors.toList());
        return new Pagina<>(convertidos, pagina, tamanho, totalElementos, totalPaginas);
    }

    public boolean vazia() {
        return itens.isEmpty();
    }
}