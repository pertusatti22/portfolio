package br.gov.anvisa.portfolio.application.usuario;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.gov.anvisa.portfolio.application.catalogo.CatalogoService;
import br.gov.anvisa.portfolio.application.usuario.dto.UsuarioRequest;
import br.gov.anvisa.portfolio.application.usuario.dto.UsuarioResponse;
import br.gov.anvisa.portfolio.domain.catalogo.CatalogoTipo;
import br.gov.anvisa.portfolio.domain.shared.RecursoNaoEncontradoException;
import br.gov.anvisa.portfolio.domain.usuario.Usuario;
import br.gov.anvisa.portfolio.domain.usuario.UsuarioPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("UsuarioService")
class UsuarioServiceTest {

    private static final Clock RELOGIO = Clock.fixed(Instant.parse("2024-06-15T12:00:00Z"), ZoneId.of("UTC"));

    @Mock
    private UsuarioPort usuarioPort;

    @Mock
    private CatalogoService catalogoService;

    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        usuarioService = new UsuarioService(usuarioPort, catalogoService, RELOGIO);
    }

    private static Usuario usuarioVigente() {
        return new Usuario("JSILVA", "NOTE-JSILVA", 1L, LocalDate.of(2024, 1, 1), null, 2L, null);
    }

    private static UsuarioRequest request(String username) {
        return new UsuarioRequest(username,
                "NOTE-JSILVA",
                1L,
                LocalDate.of(2024, 1, 1),
                null,
                2L,
                null);
    }

    @Test
    @DisplayName("valida os catalogos de tipo de usuario e de usuario de banco de dados")
    void validarReferencias() {
        when(usuarioPort.inserir(any(Usuario.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        usuarioService.criar(request("JSILVA"));

        verify(catalogoService).buscar(CatalogoTipo.TIPO_USUARIO, 1L);
        verify(catalogoService).buscar(CatalogoTipo.USUARIO_BANCO_DADOS, 2L);
    }

    @Test
    @DisplayName("considera vigente o usuario sem fim de vigencia, usando o relogio injetado")
    void vigente() {
        when(usuarioPort.buscarPorUsername(eq("JSILVA"))).thenReturn(Optional.of(usuarioVigente()));

        UsuarioResponse resposta = usuarioService.buscar("JSILVA");

        assertThat(resposta.vigente()).isTrue();
    }

    @Test
    @DisplayName("encerra a vigencia com a data corrente quando a data nao e informada")
    void inativarSemData() {
        when(usuarioPort.buscarPorUsername(eq("JSILVA"))).thenReturn(Optional.of(usuarioVigente()));
        when(usuarioPort.alterar(any(Usuario.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        UsuarioResponse resposta = usuarioService.inativar("JSILVA", null);

        assertThat(resposta.fimVigencia()).isEqualTo(LocalDate.of(2024, 6, 15));
        assertThat(resposta.vigente()).isTrue();
    }

    @Test
    @DisplayName("encerra a vigencia em data especifica")
    void inativarComData() {
        when(usuarioPort.buscarPorUsername(eq("JSILVA"))).thenReturn(Optional.of(usuarioVigente()));
        when(usuarioPort.alterar(any(Usuario.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        UsuarioResponse resposta = usuarioService.inativar("JSILVA", LocalDate.of(2024, 3, 1));

        assertThat(resposta.fimVigencia()).isEqualTo(LocalDate.of(2024, 3, 1));
        assertThat(resposta.vigente()).isFalse();
    }

    @Test
    @DisplayName("usuario inexistente gera erro de recurso nao encontrado")
    void usuarioInexistente() {
        when(usuarioPort.buscarPorUsername(eq("FANTASMA"))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.buscar("FANTASMA"))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }
}