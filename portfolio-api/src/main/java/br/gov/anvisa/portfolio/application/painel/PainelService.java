package br.gov.anvisa.portfolio.application.painel;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.anvisa.portfolio.application.painel.dto.PainelResponse;
import br.gov.anvisa.portfolio.application.painel.dto.PainelResponse.ItemContagemResponse;
import br.gov.anvisa.portfolio.domain.painel.PainelPort;
import br.gov.anvisa.portfolio.domain.painel.PainelPort.ResumoPortfolio;

/**
 * Caso de uso do painel com indicadores agregados do portifolio.
 */
@Service
public class PainelService {

    private final PainelPort painelPort;

    public PainelService(PainelPort painelPort) {
        this.painelPort = painelPort;
    }

    @Transactional(readOnly = true)
    public PainelResponse montar() {
        ResumoPortfolio resumo = painelPort.montarResumo();
        return new PainelResponse(resumo.totalSistemas(),
                resumo.sistemasAtivos(),
                resumo.sistemasNaoValidados(),
                resumo.totalServidores(),
                resumo.servidoresLigados(),
                resumo.totalUrls(),
                resumo.totalRotinas(),
                resumo.totalBackups(),
                resumo.totalUsuarios(),
                resumo.usuariosInativos(),
                converter(resumo.sistemasPorOrigem()),
                converter(resumo.sistemasPorBaseDeDado()),
                converter(resumo.urlsPorTipo()));
    }

    private java.util.List<ItemContagemResponse> converter(java.util.List<PainelPort.ItemContagem> itens) {
        return itens.stream()
                .map(item -> new ItemContagemResponse(item.rotulo(), item.total()))
                .toList();
    }
}