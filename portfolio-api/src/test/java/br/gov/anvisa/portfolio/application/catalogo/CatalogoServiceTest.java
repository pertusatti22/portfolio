package br.gov.anvisa.portfolio.application.catalogo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.gov.anvisa.portfolio.application.catalogo.dto.CatalogoRequest;
import br.gov.anvisa.portfolio.application.catalogo.dto.CatalogoResponse;
import br.gov.anvisa.portfolio.domain.catalogo.Catalogo;
import br.gov.anvisa.portfolio.domain.catalogo.CatalogoPort;
import br.gov.anvisa.portfolio.domain.catalogo.CatalogoTipo;
import br.gov.anvisa.portfolio.domain.shared.PaginaRequest;
import br.gov.anvisa.portfolio.domain.shared.RecursoNaoEncontradoException;
import br.gov.anvisa.portfolio.domain.shared.RegistroDuplicadoException;
import br.gov.anvisa.portfolio.domain.shared.RegraDeNegocioException;

@ExtendWith(MockitoExtension.class)
@DisplayName("CatalogoService")
class CatalogoServiceTest {

    @Mock
    private CatalogoPort catalogoPort;

    private CatalogoService catalogoService;

    @Captor
    private ArgumentCaptor<Catalogo> catalogoCaptor;

    @BeforeEach
    void setUp() {
        catalogoService = new CatalogoService(catalogoPort);
    }

    @Test
    @DisplayName("cria item com descricao normalizada")
    void criar() {
        when(catalogoPort.buscarPorDescricao(eq(CatalogoTipo.ORIGEM), any())).thenReturn(Optional.empty());
        when(catalogoPort.inserir(eq(CatalogoTipo.ORIGEM), any()))
                .thenAnswer(invocacao -> ((Catalogo) invocacao.getArgument(1)).comId(10L));

        CatalogoResponse criado = catalogoService.criar(CatalogoTipo.ORIGEM, new CatalogoRequest("  ANVISA  ", null, null, null));

        assertThat(criado.id()).isEqualTo(10L);
        assertThat(criado.descricao()).isEqualTo("ANVISA");
        assertThat(criado.registroAtivo()).isTrue();
    }

    @Test
    @DisplayName("recusa descricao duplicada no mesmo catalogo")
    void criarDescricaoDuplicada() {
        when(catalogoPort.buscarPorDescricao(eq(CatalogoTipo.ORIGEM), eq("ANVISA")))
                .thenReturn(Optional.of(new Catalogo(1L, "ANVISA", null, null, true)));

        assertThatThrownBy(() -> catalogoService.criar(CatalogoTipo.ORIGEM, new CatalogoRequest("ANVISA", null, null, null)))
                .isInstanceOf(RegistroDuplicadoException.class);
    }

    @Test
    @DisplayName("recusa descricao acima do tamanho da coluna do catalogo")
    void criarDescricaoLonga() {
        assertThatThrownBy(() -> catalogoService.criar(CatalogoTipo.ORIGEM,
                new CatalogoRequest("A".repeat(51), null, null, null))).isInstanceOf(RegraDeNegocioException.class);
    }

    @Test
    @DisplayName("aplica versao apenas no catalogo de software")
    void versaoPorCatalogo() {
        when(catalogoPort.buscarPorDescricao(eq(CatalogoTipo.SOFTWARE), any())).thenReturn(Optional.empty());
        when(catalogoPort.inserir(eq(CatalogoTipo.SOFTWARE), any()))
                .thenAnswer(invocacao -> ((Catalogo) invocacao.getArgument(1)).comId(3L));

        CatalogoResponse software = catalogoService.criar(CatalogoTipo.SOFTWARE,
                new CatalogoRequest("Oracle", "19c", null, null));

        assertThat(software.versao()).isEqualTo("19c");
    }

    @Test
    @DisplayName("consulta inexistente gera erro de recurso nao encontrado")
    void buscarInexistente() {
        when(catalogoPort.buscarPorId(CatalogoTipo.ORIGEM, 99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> catalogoService.buscar(CatalogoTipo.ORIGEM, 99L))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }

    @Test
    @DisplayName("resolve descricao ou nulo quando o catalogo nao existir")
    void descrever() {
        when(catalogoPort.buscarPorId(CatalogoTipo.ORIGEM, 1L))
                .thenReturn(Optional.of(new Catalogo(1L, "ANVISA", null, null, true)));

        assertThat(catalogoService.descrever(CatalogoTipo.ORIGEM, 1L)).isEqualTo("ANVISA");
        assertThat(catalogoService.descrever(CatalogoTipo.ORIGEM, null)).isNull();
    }

    @Test
    @DisplayName("lista integralmente o catalogo sem paginacao")
    void listarTodos() {
        when(catalogoPort.listarTodos(CatalogoTipo.ORIGEM, true))
                .thenReturn(List.of(new Catalogo(1L, "ANVISA", null, null, true),
                        new Catalogo(2L, "FIOCRUZ", null, null, true)));

        List<CatalogoResponse> itens = catalogoService.listarTodos(CatalogoTipo.ORIGEM, true);

        assertThat(itens).hasSize(2);
        verify(catalogoPort).listarTodos(CatalogoTipo.ORIGEM, true);
    }

    @Test
    @DisplayName("converte a pagina de dominio em resposta paginada")
    void listarPaginado() {
        when(catalogoPort.listar(eq(CatalogoTipo.ORIGEM), any(), anyBoolean(), any(PaginaRequest.class)))
                .thenReturn(br.gov.anvisa.portfolio.domain.shared.Pagina.de(
                        List.of(new Catalogo(1L, "ANVISA", null, null, true)), 0, 20, 1));

        assertThat(catalogoService.listar(CatalogoTipo.ORIGEM, null, true, PaginaRequest.padrao())
                .itens()).hasSize(1);
    }
}