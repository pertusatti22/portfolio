package br.gov.anvisa.portfolio.api.sistema;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import br.gov.anvisa.portfolio.application.sistema.SistemaDetalheService;
import br.gov.anvisa.portfolio.application.sistema.SistemaService;
import br.gov.anvisa.portfolio.application.sistema.dto.SistemaRequest;
import br.gov.anvisa.portfolio.application.sistema.dto.SistemaResponse;
import br.gov.anvisa.portfolio.config.AppConfig;
import br.gov.anvisa.portfolio.domain.shared.Pagina;
import br.gov.anvisa.portfolio.domain.shared.RegistroDuplicadoException;
import br.gov.anvisa.portfolio.domain.sistema.SistemaFiltro;

@WebMvcTest(SistemaController.class)
@Import(AppConfig.class)
@TestPropertySource(properties = "portfolio.api.base-path=/api/v1")
@DisplayName("SistemaController")
class SistemaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SistemaService sistemaService;

    @MockBean
    private SistemaDetalheService detalheService;

    private static SistemaResponse resposta() {
        return new SistemaResponse(15L,
                "SGAS",
                "Sistema de Gestao",
                1L,
                "ANVISA",
                "Objetivo",
                null,
                true,
                false,
                "Software",
                2L,
                "Oracle",
                true,
                false);
    }

    @Test
    @DisplayName("cria sistema devolvendo 201 e o recurso criado")
    void criar() throws Exception {
        when(sistemaService.criar(any(SistemaRequest.class))).thenReturn(resposta());

        mockMvc.perform(post("/api/v1/sistemas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"sigla":"SGAS","nome":"Sistema de Gestao","origemId":1,"baseDeDadoId":2}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/sistemas/15"))
                .andExpect(jsonPath("$.id").value(15))
                .andExpect(jsonPath("$.sigla").value("SGAS"));
    }

    @Test
    @DisplayName("devolve 400 com os campos invalidos")
    void criarComDadosInvalidos() throws Exception {
        mockMvc.perform(post("/api/v1/sistemas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("bad_request"))
                .andExpect(jsonPath("$.campos.length()").value(2))
                .andExpect(jsonPath("$.campos[?(@.campo == 'sigla')].mensagem").exists())
                .andExpect(jsonPath("$.campos[?(@.campo == 'nome')].mensagem").exists());
    }

    @Test
    @DisplayName("traduz sigla duplicada para 409")
    void siglaDuplicada() throws Exception {
        when(sistemaService.criar(any(SistemaRequest.class)))
                .thenThrow(new RegistroDuplicadoException("Ja existe um sistema com a sigla SGAS"));

        mockMvc.perform(post("/api/v1/sistemas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"sigla":"SGAS","nome":"Sistema"}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.erro").value("conflict"));
    }

    @Test
    @DisplayName("devolve 400 quando a pagina nao e numerica")
    void paginaInvalida() throws Exception {
        mockMvc.perform(get("/api/v1/sistemas").param("pagina", "abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("bad_request"))
                .andExpect(jsonPath("$.mensagem").value(org.hamcrest.Matchers.containsString("pagina")));
    }

    @Test
    @DisplayName("encaminha os filtros de listagem para o servico")
    void listarComFiltros() throws Exception {
        when(sistemaService.listar(any(SistemaFiltro.class)))
                .thenReturn(Pagina.de(java.util.List.of(resposta()), 0, 20, 1));

        mockMvc.perform(get("/api/v1/sistemas")
                        .param("termo", "gestao")
                        .param("origemId", "1")
                        .param("ativo", "true")
                        .param("tamanho", "10")
                        .param("ordenacao", "sigla")
                        .param("crescente", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conteudo.length()").value(1))
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.pagina").value(0));
    }

    @Test
    @DisplayName("altera o registro ativo do sistema")
    void alterarAtivo() throws Exception {
        when(sistemaService.alterarAtivo(15L, false)).thenReturn(new SistemaResponse(15L,
                "SGAS",
                "Sistema",
                null,
                null,
                null,
                null,
                true,
                false,
                null,
                null,
                null,
                false,
                false));

        mockMvc.perform(patch("/api/v1/sistemas/15/ativo").param("ativo", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registroAtivo").value(false));
    }

    @Test
    @DisplayName("exige o parametro ativo na alteracao de situacao")
    void ativoObrigatorio() throws Exception {
        mockMvc.perform(patch("/api/v1/sistemas/15/ativo"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value(org.hamcrest.Matchers.containsString("ativo")));
    }
}