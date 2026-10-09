package br.gov.anvisa.portfolio.domain.shared;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Textos")
class TextosTest {

    @Test
    @DisplayName("normaliza espaços nas pontas")
    void normalizaEspacos() {
        assertThat(Textos.normalizar("  ORACLE  ")).isEqualTo("ORACLE");
        assertThat(Textos.normalizar(null)).isNull();
    }

    @Test
    @DisplayName("obrigatorio remove espaços das pontas")
    void obrigatorioNormaliza() {
        assertThat(Textos.obrigatorio("  Oracle ", "Banco", 20)).isEqualTo("Oracle");
    }

    @Test
    @DisplayName("obrigatorio recusa valor vazio")
    void obrigatorioRecusaVazio() {
        assertThatThrownBy(() -> Textos.obrigatorio("   ", "Banco", 10))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("Banco");
    }

    @Test
    @DisplayName("obrigatorio recusa valor acima do tamanho maximo")
    void obrigatorioRecusaTamanho() {
        assertThatThrownBy(() -> Textos.obrigatorio("123456", "Banco", 5))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("5");
    }

    @Test
    @DisplayName("opcional devolve nulo para texto vazio")
    void opcionalVazio() {
        assertThat(Textos.opcional("  ", 10)).isNull();
        assertThat(Textos.opcional(null, 10)).isNull();
    }

    @Test
    @DisplayName("codigo converte para caixa alta")
    void codigoCaixaAlta() {
        assertThat(Textos.codigo(" sgas ", "Sigla", 10)).isEqualTo("SGAS");
    }
}