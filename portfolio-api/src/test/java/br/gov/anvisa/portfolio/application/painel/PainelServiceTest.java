package br.gov.anvisa.portfolio.application.painel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.gov.anvisa.portfolio.application.painel.dto.PainelResponse;
import br.gov.anvisa.portfolio.domain.painel.PainelPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("PainelService")
class PainelServiceTest {

    @Mock
    private PainelPort painelPort;

    @InjectMocks
    private PainelService painelService;

    @Test
    @DisplayName("converte o resumo do dominio em indicadores da API")
    void montar() {
        PainelPort.ResumoPortfolio resumo = new PainelPort.ResumoPortfolio(20,
                17,
                3,
                7,
                5,
                42,
                3,
                2,
                10,
                1,
                List.of(new PainelPort.ItemContagem("ANVISA", 12), new PainelPort.ItemContagem("FIOCRUZ", 8)),
                List.of(new PainelPort.ItemContagem("Oracle", 15)),
                List.of(new PainelPort.ItemContagem("Producao", 20)));

        when(painelPort.montarResumo()).thenReturn(resumo);

        PainelResponse resposta = painelService.montar();

        assertThat(resposta.totalSistemas()).isEqualTo(20);
        assertThat(resposta.sistemasNaoValidados()).isEqualTo(3);
        assertThat(resposta.sistemasPorOrigem()).hasSize(2);
        assertThat(resposta.sistemasPorOrigem().get(0).rotulo()).isEqualTo("ANVISA");
        assertThat(resposta.sistemasPorOrigem().get(0).total()).isEqualTo(12);
        assertThat(resposta.sistemasPorBaseDeDado()).singleElement()
                .satisfies(item -> assertThat(item.rotulo()).isEqualTo("Oracle"));
        assertThat(resposta.urlsPorTipo()).hasSize(1);
    }
}