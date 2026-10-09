package br.gov.anvisa.portfolio.domain.usuario;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.gov.anvisa.portfolio.domain.shared.RegraDeNegocioException;

@DisplayName("Usuario")
class UsuarioTest {

    private static final LocalDate INICIO = LocalDate.of(2024, 1, 1);

    @Test
    @DisplayName("normaliza o login para caixa alta")
    void normalizaLogin() {
        Usuario usuario = new Usuario(" jsilva ", "NOTE-JSILVA", 1L, INICIO, null, 2L, null);

        assertThat(usuario.username()).isEqualTo("JSILVA");
        assertThat(usuario.usuarioSecundario()).isNull();
    }

    @Test
    @DisplayName("recusa fim de vigencia anterior ao inicio")
    void vigenciaInconsistente() {
        assertThatThrownBy(() -> new Usuario("JSILVA", "HOST", 1L, INICIO, INICIO.minusDays(1), 2L, null))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("vigencia");
    }

    @Test
    @DisplayName("usuario sem fim de vigencia esta vigente")
    void vigenteSemFim() {
        Usuario usuario = new Usuario("JSILVA", "HOST", 1L, INICIO, null, 2L, null);

        assertThat(usuario.vigente(LocalDate.of(2030, 1, 1))).isTrue();
    }

    @Test
    @DisplayName("usuario com fim de vigencia passado nao esta vigente")
    void vigenteAposFim() {
        Usuario usuario = new Usuario("JSILVA", "HOST", 1L, INICIO, LocalDate.of(2024, 6, 30), 2L, null);

        assertThat(usuario.vigente(LocalDate.of(2024, 7, 1))).isFalse();
    }

    @Test
    @DisplayName("tipo de operacao e resolvido pelo codigo do banco")
    void tipoOperacao() {
        assertThat(TipoOperacao.doCodigo('i')).isEqualTo(TipoOperacao.INSERCAO);
        assertThat(TipoOperacao.doCodigo('U')).isEqualTo(TipoOperacao.ALTERACAO);
        assertThat(TipoOperacao.doCodigo('D')).isEqualTo(TipoOperacao.EXCLUSAO);
        assertThatThrownBy(() -> TipoOperacao.doCodigo('X')).isInstanceOf(RegraDeNegocioException.class);
    }
}