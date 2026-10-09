package br.gov.anvisa.portfolio.api.painel;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import br.gov.anvisa.portfolio.application.painel.PainelService;
import br.gov.anvisa.portfolio.application.painel.dto.PainelResponse;
import br.gov.anvisa.portfolio.config.AppConfig;
import br.gov.anvisa.portfolio.domain.shared.RecursoNaoEncontradoException;
import br.gov.anvisa.portfolio.domain.shared.RegraDeNegocioException;

@WebMvcTest(PainelController.class)
@Import(AppConfig.class)
@TestPropertySource(properties = "portfolio.api.base-path=/api/v1")
@DisplayName("PainelController")
class PainelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PainelService painelService;

    private static PainelResponse resposta(long totalSistemas) {
        return new PainelResponse(totalSistemas,
                totalSistemas,
                1,
                7,
                5,
                42,
                3,
                2,
                10,
                1,
                List.of(new PainelResponse.ItemContagemResponse("ANVISA", totalSistemas)),
                List.of(),
                List.of(new PainelResponse.ItemContagemResponse("Producao", 12)));
    }

    @Test
    @DisplayName("devolve os indicadores do painel")
    void resumo() throws Exception {
        when(painelService.montar()).thenReturn(resposta(20L));

        mockMvc.perform(get("/api/v1/painel/resumo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSistemas").value(20))
                .andExpect(jsonPath("$.totalServidores").value(7))
                .andExpect(jsonPath("$.sistemasPorOrigem[0].rotulo").value("ANVISA"))
                .andExpect(jsonPath("$.urlsPorTipo[0].total").value(12));
    }

    @Test
    @DisplayName("traduz recurso inexistente para 404")
    void recursoInexistente() throws Exception {
        when(painelService.montar()).thenThrow(new RecursoNaoEncontradoException("Painel", 1L));

        mockMvc.perform(get("/api/v1/painel/resumo"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.erro").value("not_found"))
                .andExpect(jsonPath("$.mensagem").value("Painel nao encontrado(a): 1"))
                .andExpect(jsonPath("$.caminho").value("/api/v1/painel/resumo"));
    }

    @Test
    @DisplayName("traduz regra de negocio para 422")
    void regraDeNegocio() throws Exception {
        when(painelService.montar()).thenThrow(new RegraDeNegocioException("Indicador invalido"));

        mockMvc.perform(get("/api/v1/painel/resumo"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.erro").value("business_rule"))
                .andExpect(jsonPath("$.mensagem").value("Indicador invalido"));
    }

    @Test
    @DisplayName("traduz falha inesperada para 500 sem expor detalhes")
    void falhaInesperada() throws Exception {
        when(painelService.montar()).thenThrow(new IllegalStateException("detalhe interno"));

        mockMvc.perform(get("/api/v1/painel/resumo"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.erro").value("internal_error"))
                .andExpect(jsonPath("$.mensagem").value("Erro inesperado"))
                .andExpect(jsonPath("$.mensagem").value(
                        org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("detalhe interno"))));
    }
}
