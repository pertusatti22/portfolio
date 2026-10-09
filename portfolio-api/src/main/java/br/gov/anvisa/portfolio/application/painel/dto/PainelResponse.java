package br.gov.anvisa.portfolio.application.painel.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Indicadores agregados exibidos no painel inicial.
 */
@Schema(name = "PainelResponse", description = "Indicadores do portifolio")
public record PainelResponse(long totalSistemas,
                             long sistemasAtivos,
                             long sistemasNaoValidados,
                             long totalServidores,
                             long servidoresLigados,
                             long totalUrls,
                             long totalRotinas,
                             long totalBackups,
                             long totalUsuarios,
                             long usuariosInativos,
                             List<ItemContagemResponse> sistemasPorOrigem,
                             List<ItemContagemResponse> sistemasPorBaseDeDado,
                             List<ItemContagemResponse> urlsPorTipo) {

    /**
     * Item de um grafico de contagem.
     */
    @Schema(name = "ItemContagemResponse", description = "Rotulo e total de um agrupamento")
    public record ItemContagemResponse(String rotulo, long total) {
    }
}