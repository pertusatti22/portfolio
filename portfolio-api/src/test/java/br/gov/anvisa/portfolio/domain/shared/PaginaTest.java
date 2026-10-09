package br.gov.anvisa.portfolio.domain.shared;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Pagina")
class PaginaTest {

    @Test
    @DisplayName("calcula a quantidade total de paginas")
    void calculaTotalPaginas() {
        Pagina<String> pagina = Pagina.de(List.of("a", "b", "c"), 0, 2, 5);

        assertThat(pagina.totalPaginas()).isEqualTo(3);
        assertThat(pagina.totalElementos()).isEqualTo(5);
        assertThat(pagina.vazia()).isFalse();
    }

    @Test
    @DisplayName("mantém zero paginas quando o filtro nao retorna itens")
    void filtroVazio() {
        Pagina<String> pagina = Pagina.de(List.of(), 0, 20, 0);

        assertThat(pagina.totalPaginas()).isZero();
        assertThat(pagina.vazia()).isTrue();
    }

    @Test
    @DisplayName("converte os itens preservando os metadados de paginacao")
    void converteItens() {
        Pagina<String> origem = Pagina.de(List.of("1", "2"), 1, 2, 4);
        Pagina<Integer> destino = origem.map(String::length);

        assertThat(destino.itens()).containsExactly(1, 1);
        assertThat(destino.pagina()).isEqualTo(1);
        assertThat(destino.totalElementos()).isEqualTo(4);
    }

    @Test
    @DisplayName("pagina unitaria")
    void unitaria() {
        Pagina<String> pagina = Pagina.unica(List.of("unico"));

        assertThat(pagina.totalPaginas()).isEqualTo(1);
        assertThat(pagina.totalElementos()).isEqualTo(1);
    }
}