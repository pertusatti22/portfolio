package br.gov.anvisa.portfolio.domain.sistema;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.gov.anvisa.portfolio.domain.shared.RegraDeNegocioException;

@DisplayName("Sistema")
class SistemaTest {

    private static Sistema.Dados dados(String sigla, String nome) {
        return new Sistema.Dados(sigla, nome, 1L, "Objetivo", null, true, false, "Software", 2L);
    }

    @Test
    @DisplayName("cria sistema ativo e nao validado, com sigla em caixa alta")
    void criacao() {
        Sistema sistema = Sistema.criar(dados(" sgas ", "Sistema de Gestao"));

        assertThat(sistema.sigla()).isEqualTo("SGAS");
        assertThat(sistema.nome()).isEqualTo("Sistema de Gestao");
        assertThat(sistema.registroAtivo()).isTrue();
        assertThat(sistema.validado()).isFalse();
        assertThat(sistema.id()).isNull();
    }

    @Test
    @DisplayName("exige sigla preenchida")
    void siglaObrigatoria() {
        assertThatThrownBy(() -> Sistema.criar(dados("  ", "Sistema")))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("Sigla");
    }

    @Test
    @DisplayName("exige nome preenchido")
    void nomeObrigatorio() {
        assertThatThrownBy(() -> Sistema.criar(dados("SGAS", "")))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("Nome");
    }

    @Test
    @DisplayName("valida e alterna o registro ativo")
    void validarEAtivar() {
        Sistema sistema = Sistema.criar(dados("SGAS", "Sistema"));

        sistema.validar();
        assertThat(sistema.validado()).isTrue();

        sistema.desativar();
        assertThat(sistema.registroAtivo()).isFalse();

        sistema.ativar();
        assertThat(sistema.registroAtivo()).isTrue();
    }

    @Test
    @DisplayName("reconstitui preservando identificador e indicadores")
    void reconstituicao() {
        Sistema sistema = Sistema.reconstituir(7L, dados("SGAS", "Sistema"), false, true);

        assertThat(sistema.id()).isEqualTo(7L);
        assertThat(sistema.registroAtivo()).isFalse();
        assertThat(sistema.validado()).isTrue();
    }

    @Test
    @DisplayName("normaliza textos opcionais vazios para nulo")
    void normalizaOpcionais() {
        Sistema sistema = Sistema.criar(new Sistema.Dados("SGAS", "Sistema", null, "  ", "   ", false, false, null, null));

        assertThat(sistema.objetivo()).isNull();
        assertThat(sistema.arquivo()).isNull();
        assertThat(sistema.classificacaoBem()).isNull();
    }
}