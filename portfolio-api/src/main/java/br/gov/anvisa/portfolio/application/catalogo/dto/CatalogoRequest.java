package br.gov.anvisa.portfolio.application.catalogo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload de criacao/alteracao de um item de catalogo.
 */
@Schema(name = "CatalogoRequest", description = "Item de catalogo de dados de referencia")
public record CatalogoRequest(

        @Schema(description = "Descricao do item", example = "ANVISA")
        @NotBlank(message = "A descricao e obrigatoria")
        @Size(max = 255, message = "A descricao deve ter no maximo 255 caracteres")
        String descricao,

        @Schema(description = "Versao (aplicavel apenas ao catalogo de softwares)", example = "17.3.1")
        @Size(max = 10)
        String versao,

        @Schema(description = "Indicador de liberacao (programa autorizado / usuario liberado)")
        Boolean liberado,

        @Schema(description = "Registro ativo", defaultValue = "true")
        Boolean ativo) {

    public boolean registroAtivo() {
        return ativo == null || ativo;
    }

    public Boolean liberado() {
        return liberado != null && liberado;
    }
}