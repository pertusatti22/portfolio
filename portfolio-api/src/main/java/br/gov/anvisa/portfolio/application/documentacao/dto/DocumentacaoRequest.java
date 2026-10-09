package br.gov.anvisa.portfolio.application.documentacao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Payload de criacao/alteracao de uma documentacao.
 */
@Schema(name = "DocumentacaoRequest", description = "Link de documentacao de um sistema")
public record DocumentacaoRequest(

        @Schema(description = "URL onde esta hospedada a documentacao", example = "https://manuais.exemplo.gov.br/sgas")
        @NotBlank(message = "A URL do repositorio e obrigatoria")
        @Size(max = 600, message = "A URL deve ter no maximo 600 caracteres")
        String urlRepositorio,

        @Schema(description = "Codigo do catalogo de tipo de documentacao")
        @NotNull(message = "O tipo de documentacao e obrigatorio")
        Long tipoDocumentacaoId,

        @Schema(description = "Registro ativo", defaultValue = "true")
        Boolean ativo) {

    public boolean registroAtivo() {
        return ativo == null || ativo;
    }
}