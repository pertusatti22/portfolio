package br.gov.anvisa.portfolio.application.sistema.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Schema vinculado a um sistema, com indicacao de schema principal.
 */
@Schema(name = "SchemaVinculadoResponse", description = "Schema de acesso do sistema")
public record SchemaVinculadoResponse(Long schemaId,
                                       String descricao,
                                       boolean principal) {
}