package br.gov.anvisa.portfolio.application.sistema;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.gov.anvisa.portfolio.application.catalogo.CatalogoService;
import br.gov.anvisa.portfolio.application.catalogo.dto.CatalogoResponse;
import br.gov.anvisa.portfolio.application.sistema.dto.SchemaVinculadoResponse;
import br.gov.anvisa.portfolio.domain.catalogo.CatalogoTipo;
import br.gov.anvisa.portfolio.domain.sistema.Sistema;
import br.gov.anvisa.portfolio.domain.servidor.Servidor;
import br.gov.anvisa.portfolio.domain.servidor.ServidorPort;
import br.gov.anvisa.portfolio.domain.shared.RecursoNaoEncontradoException;
import br.gov.anvisa.portfolio.domain.shared.RegraDeNegocioException;
import br.gov.anvisa.portfolio.domain.url.UrlPort;
import br.gov.anvisa.portfolio.domain.vinculo.TipoVinculo;
import br.gov.anvisa.portfolio.domain.vinculo.VinculoPort;
import br.gov.anvisa.portfolio.domain.sistema.SistemaPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("SistemaVinculosService")
class SistemaVinculosServiceTest {

    @Mock
    private VinculoPort vinculoPort;

    @Mock
    private SistemaPort sistemaPort;

    @Mock
    private ServidorPort servidorPort;

    @Mock
    private UrlPort urlPort;

    @Mock
    private CatalogoService catalogoService;

    private SistemaVinculosService vinculosService;

    private static Sistema sistema() {
        return Sistema.reconstituir(1L,
                new Sistema.Dados("SGAS", "Sistema", 1L, null, null, true, false, null, 2L),
                true,
                false);
    }

    @BeforeEach
    void setUp() {
        vinculosService = new SistemaVinculosService(vinculoPort, sistemaPort, servidorPort, urlPort, catalogoService);
    }

    @Test
    @DisplayName("lista os schemas vinculados marcando o principal")
    void listarSchemas() {
        when(vinculoPort.listarPrincipais(1L, TipoVinculo.SCHEMA)).thenReturn(List.of(3L));
        when(vinculoPort.listarVinculados(1L, TipoVinculo.SCHEMA)).thenReturn(List.of(3L, 4L));
        when(catalogoService.descrever(eq(CatalogoTipo.SCHEMA), any())).thenReturn("SCHEMA");

        List<SchemaVinculadoResponse> schemas = vinculosService.listarSchemas(1L);

        assertThat(schemas).hasSize(2);
        assertThat(schemas.get(0).schemaId()).isEqualTo(3L);
        assertThat(schemas.get(0).principal()).isTrue();
        assertThat(schemas.get(1).principal()).isFalse();
    }

    @Test
    @DisplayName("recusa schema principal fora do conjunto vinculado")
    void schemaPrincipalForaDoConjunto() {
        assertThatThrownBy(() -> vinculosService.substituirSchemas(1L, List.of(3L, 4L), 9L))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("principal");
    }

    @Test
    @DisplayName("substitui o conjunto de schemas e define o principal")
    void substituirSchemas() {
        when(sistemaPort.buscarPorId(1L)).thenReturn(Optional.of(sistema()));

        List<SchemaVinculadoResponse> schemas = vinculosService.substituirSchemas(1L, List.of(3L, 4L), 3L);

        verify(catalogoService).buscar(CatalogoTipo.SCHEMA, 3L);
        verify(catalogoService).buscar(CatalogoTipo.SCHEMA, 4L);
        verify(vinculoPort).substituirVinculos(1L, TipoVinculo.SCHEMA, List.of(3L, 4L));
        verify(vinculoPort).definirPrincipal(1L, TipoVinculo.SCHEMA, 3L);
    }

    @Test
    @DisplayName("valida os softwares vinculados no catalogo")
    void substituirSoftwares() {
        when(sistemaPort.buscarPorId(1L)).thenReturn(Optional.of(sistema()));
        when(vinculoPort.listarVinculados(1L, TipoVinculo.SOFTWARE)).thenReturn(List.of(7L));
        when(catalogoService.buscar(CatalogoTipo.SOFTWARE, 7L))
                .thenReturn(new CatalogoResponse(7L, "Oracle", "19c", null, true));

        List<CatalogoResponse> softwares = vinculosService.substituirSoftwares(1L, List.of(7L));

        verify(vinculoPort).substituirVinculos(1L, TipoVinculo.SOFTWARE, List.of(7L));
        assertThat(softwares).singleElement()
                .satisfies(software -> {
                    assertThat(software.descricao()).isEqualTo("Oracle");
                    assertThat(software.versao()).isEqualTo("19c");
                });
    }

    @Test
    @DisplayName("recusa servidor inexistente na substituicao")
    void servidorInexistente() {
        when(servidorPort.buscarPorId(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vinculosService.substituirServidores(1L, List.of(42L)))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }

    @Test
    @DisplayName("substitui os servidores vinculados")
    void substituirServidores() {
        when(sistemaPort.buscarPorId(1L)).thenReturn(Optional.of(sistema()));
        when(servidorPort.buscarPorId(42L)).thenReturn(Optional.of(servidor()));
        when(vinculoPort.listarVinculados(1L, TipoVinculo.SERVIDOR)).thenReturn(List.of(42L));

        assertThat(vinculosService.substituirServidores(1L, List.of(42L))).containsExactly(42L);
        verify(vinculoPort).substituirVinculos(1L, TipoVinculo.SERVIDOR, List.of(42L));
    }

    @Test
    @DisplayName("recusa URL inexistente na substituicao")
    void urlInexistente() {
        when(urlPort.buscarPorId(8L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vinculosService.substituirUrls(1L, List.of(8L)))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }

    @Test
    @DisplayName("recusa vincular a sistema inexistente")
    void sistemaInexistente() {
        when(sistemaPort.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vinculosService.listarUrls(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }

    private static Servidor servidor() {
        return Servidor.reconstituir(42L,
                new Servidor.Dados("SRV01", true, "8", "32", 3L, "500", 4L, 5L, "Portal"),
                false);
    }
}