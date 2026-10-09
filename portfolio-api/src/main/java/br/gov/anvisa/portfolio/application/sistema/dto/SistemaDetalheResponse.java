package br.gov.anvisa.portfolio.application.sistema.dto;

import java.util.List;

import br.gov.anvisa.portfolio.application.catalogo.dto.CatalogoResponse;
import br.gov.anvisa.portfolio.application.componente.dto.ComponenteResponse;
import br.gov.anvisa.portfolio.application.documentacao.dto.DocumentacaoResponse;
import br.gov.anvisa.portfolio.application.servidor.dto.ServidorResponse;
import br.gov.anvisa.portfolio.application.url.dto.UrlResponse;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Visao completa de um sistema, com seus relacionamentos.
 */
@Schema(name = "SistemaDetalheResponse", description = "Sistema com componentes, documentacao e vinculos")
public record SistemaDetalheResponse(SistemaResponse sistema,
                                     List<ComponenteResponse> componentes,
                                     List<DocumentacaoResponse> documentacoes,
                                     List<SchemaVinculadoResponse> schemas,
                                     List<CatalogoResponse> softwares,
                                     List<ServidorResponse> servidores,
                                     List<UrlResponse> urls) {
}