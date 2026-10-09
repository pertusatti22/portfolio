package br.gov.anvisa.portfolio.application.sistema;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.gov.anvisa.portfolio.application.catalogo.CatalogoService;
import br.gov.anvisa.portfolio.application.sistema.dto.SistemaRequest;
import br.gov.anvisa.portfolio.application.sistema.dto.SistemaResponse;
import br.gov.anvisa.portfolio.domain.catalogo.CatalogoTipo;
import br.gov.anvisa.portfolio.domain.sistema.Sistema;
import br.gov.anvisa.portfolio.domain.sistema.SistemaPort;
import br.gov.anvisa.portfolio.domain.shared.RecursoNaoEncontradoException;
import br.gov.anvisa.portfolio.domain.shared.RegistroDuplicadoException;

@ExtendWith(MockitoExtension.class)
@DisplayName("SistemaService")
class SistemaServiceTest {

    @Mock
    private SistemaPort sistemaPort;

    @Mock
    private CatalogoService catalogoService;

    private SistemaService sistemaService;

    @Captor
    private ArgumentCaptor<Sistema> sistemaCaptor;

    @BeforeEach
    void setUp() {
        sistemaService = new SistemaService(sistemaPort, catalogoService);
    }

    private static SistemaRequest request(String sigla, String nome) {
        return new SistemaRequest(sigla, nome, 1L, "Objetivo", null, true, false, "Software", 2L, true, false);
    }

    @Test
    @DisplayName("cria sistema normalizando a sigla")
    void criar() {
        when(sistemaPort.buscarPorSigla(any())).thenReturn(Optional.empty());
        when(sistemaPort.inserir(any(Sistema.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        sistemaService.criar(request(" sgas ", "Sistema de Gestao"));

        verify(sistemaPort).inserir(sistemaCaptor.capture());
        assertThat(sistemaCaptor.getValue().sigla()).isEqualTo("SGAS");
    }

    @Test
    @DisplayName("recusa sigla ja cadastrada")
    void criarSiglaDuplicada() {
        Sistema existente = Sistema.reconstituir(1L, dados("SGAS", "Sistema"), true, false);
        when(sistemaPort.buscarPorSigla(eq("SGAS"))).thenReturn(Optional.of(existente));

        assertThatThrownBy(() -> sistemaService.criar(request("SGAS", "Sistema")))
                .isInstanceOf(RegistroDuplicadoException.class);
    }

    @Test
    @DisplayName("valida o catalogo de origem e de base de dado")
    void validarCatalogos() {
        when(sistemaPort.buscarPorSigla(any())).thenReturn(Optional.empty());
        when(sistemaPort.inserir(any(Sistema.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        sistemaService.criar(request("SGAS", "Sistema"));

        verify(catalogoService).buscar(CatalogoTipo.ORIGEM, 1L);
        verify(catalogoService).buscar(CatalogoTipo.BASE_DE_DADO, 2L);
    }

    @Test
    @DisplayName("consulta inexistente gera erro de recurso nao encontrado")
    void buscarInexistente() {
        when(sistemaPort.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sistemaService.buscar(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }

    @Test
    @DisplayName("marca o sistema como validado")
    void validar() {
        when(sistemaPort.buscarPorId(1L)).thenReturn(Optional.of(Sistema.reconstituir(1L, dados("SGAS", "Sistema"), true, false)));
        when(sistemaPort.alterar(any(Sistema.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        SistemaResponse resposta = sistemaService.validar(1L);

        assertThat(resposta.validado()).isTrue();
    }

    @Test
    @DisplayName("desativa o sistema mantendo os demais dados")
    void desativar() {
        when(sistemaPort.buscarPorId(1L)).thenReturn(Optional.of(Sistema.reconstituir(1L, dados("SGAS", "Sistema"), true, false)));
        when(sistemaPort.alterar(any(Sistema.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        SistemaResponse resposta = sistemaService.alterarAtivo(1L, false);

        assertThat(resposta.registroAtivo()).isFalse();
        assertThat(resposta.sigla()).isEqualTo("SGAS");
    }

    private static Sistema.Dados dados(String sigla, String nome) {
        return new Sistema.Dados(sigla, nome, 1L, "Objetivo", null, true, false, "Software", 2L);
    }
}