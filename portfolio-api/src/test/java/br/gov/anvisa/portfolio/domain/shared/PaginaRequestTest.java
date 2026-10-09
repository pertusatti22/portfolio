package br.gov.anvisa.portfolio.domain.shared;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("PaginaRequest")
class PaginaRequestTest {

    @Test
    @DisplayName("corrige pagina negativa e tamanho invalido")
    void normalizaValores() {
        PaginaRequest requisicao = new PaginaRequest(-5, 0, "nome", true);

        assertThat(requisicao.pagina()).isZero();
        assertThat(requisicao.tamanho()).isEqualTo(PaginaRequest.TAMANHO_PADRAO);
    }

    @Test
    @DisplayName("limita o tamanho ao maximo permitido")
    void limitaTamanho() {
        PaginaRequest requisicao = new PaginaRequest(0, 5000, "nome", true);

        assertThat(requisicao.tamanho()).isEqualTo(PaginaRequest.TAMANHO_MAXIMO);
    }
}