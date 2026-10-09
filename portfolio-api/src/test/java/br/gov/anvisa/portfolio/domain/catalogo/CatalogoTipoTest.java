package br.gov.anvisa.portfolio.domain.catalogo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import br.gov.anvisa.portfolio.domain.shared.RecursoNaoEncontradoException;

@DisplayName("CatalogoTipo")
class CatalogoTipoTest {

    @ParameterizedTest
    @DisplayName("resolve o catalogo pelo segmento de URL, ignorando caixa")
    @ValueSource(strings = {"origens", "ORIGENS", "bases-de-dado", "tipos-usuario"})
    void resolvePorRecurso(String recurso) {
        assertThat(CatalogoTipo.doRecurso(recurso)).isNotNull();
    }

    @Test
    @DisplayName("recurso desconhecido gera erro de recurso nao encontrado")
    void recursoDesconhecido() {
        assertThatThrownBy(() -> CatalogoTipo.doRecurso("inexistente"))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }

    @Test
    @DisplayName("apenas software possui versao")
    void versaoSomenteSoftware() {
        assertThat(CatalogoTipo.SOFTWARE.possuiVersao()).isTrue();
        assertThat(CatalogoTipo.ORIGEM.possuiVersao()).isFalse();
    }

    @Test
    @DisplayName("programa e usuario de banco de dados possuem indicador de liberacao")
    void liberacao() {
        assertThat(CatalogoTipo.PROGRAMA.possuiIndicadorLiberacao()).isTrue();
        assertThat(CatalogoTipo.USUARIO_BANCO_DADOS.possuiIndicadorLiberacao()).isTrue();
        assertThat(CatalogoTipo.AMBIENTE.possuiIndicadorLiberacao()).isFalse();
    }

    @Test
    @DisplayName("todos os catalogos possuem recurso e rotulo preenchidos")
    void metadadosPreenchidos() {
        assertThat(CatalogoTipo.values())
                .allSatisfy(tipo -> {
                    assertThat(tipo.recurso()).isNotBlank();
                    assertThat(tipo.rotulo()).isNotBlank();
                    assertThat(tipo.tamanhoDescricao()).isPositive();
                });
    }
}