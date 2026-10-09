package br.gov.anvisa.portfolio.api.painel;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gov.anvisa.portfolio.application.painel.PainelService;
import br.gov.anvisa.portfolio.application.painel.dto.PainelResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Endpoint do painel com indicadores agregados do portifolio.
 */
@Tag(name = "Painel", description = "Indicadores agregados do portifolio")
@RestController
@RequestMapping("${portfolio.api.base-path}/painel")
public class PainelController {

    private final PainelService painelService;

    public PainelController(PainelService painelService) {
        this.painelService = painelService;
    }

    @GetMapping("/resumo")
    @Operation(summary = "Resume os indicadores do portifolio")
    public PainelResponse resumo() {
        return painelService.montar();
    }
}