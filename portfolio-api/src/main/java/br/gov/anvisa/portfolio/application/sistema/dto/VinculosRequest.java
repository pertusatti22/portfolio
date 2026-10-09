package br.gov.anvisa.portfolio.application.sistema.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Payload de substituicao do conjunto de vinculos (schemas, urls, softwares, servidores, bases de dado).
 */
@Schema(name = "VinculosRequest", description = "Conjunto de identificadores vinculados")
public record VinculosRequest(

        @Schema(description = "Identificadores vinculados (substituem o conjunto atual)")
        @NotNull(message = "A lista de vinculos e obrigatoria")
        List<Long> ids,

        @Schema(description = "Identificador do vinculo principal (aplicavel a schemas)")
        Long principalId) {

    public List<Long> ids() {
        return ids == null ? List.of() : List.copyOf(ids);
    }
}