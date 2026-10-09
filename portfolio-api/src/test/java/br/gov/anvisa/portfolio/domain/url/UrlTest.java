package br.gov.anvisa.portfolio.domain.url;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.gov.anvisa.portfolio.domain.shared.RegraDeNegocioException;

@DisplayName("Url")
class UrlTest {

    private static Url.Dados dados(String endereco) {
        return new Url.Dados(endereco, 1L, 2L, 3L, 4L);
    }

    @Test
    @DisplayName("cria URL ativa, assumindo desenvolvimento como tipo padrao")
    void criacao() {
        Url url = Url.criar(null, dados("https://homer.example.gov.br"));

        assertThat(url.tipo()).isEqualTo(TipoUrl.DESENVOLVIMENTO);
        assertThat(url.registroAtivo()).isTrue();
        assertThat(url.endereco()).isEqualTo("https://homer.example.gov.br");
    }

    @Test
    @DisplayName("exige endereco preenchido")
    void enderecoObrigatorio() {
        assertThatThrownBy(() -> Url.criar(TipoUrl.PRODUCAO, dados("  ")))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("URL");
    }

    @Test
    @DisplayName("reconstitui preservando identificador, tipo e indicador")
    void reconstituicao() {
        Url url = Url.reconstituir(3L, TipoUrl.PRODUCAO, dados("https://app.example.gov.br"), false);

        assertThat(url.id()).isEqualTo(3L);
        assertThat(url.tipo()).isEqualTo(TipoUrl.PRODUCAO);
        assertThat(url.registroAtivo()).isFalse();
    }

    @Test
    @DisplayName("altera tipo e dados")
    void alteracao() {
        Url url = Url.criar(TipoUrl.DESENVOLVIMENTO, dados("https://dev.example.gov.br"));

        url.atualizarDados(TipoUrl.HOMOLOGACAO, dados("https://hml.example.gov.br"));

        assertThat(url.tipo()).isEqualTo(TipoUrl.HOMOLOGACAO);
        assertThat(url.endereco()).isEqualTo("https://hml.example.gov.br");
    }
}