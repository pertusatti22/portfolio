package br.gov.anvisa.portfolio.application.componente.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload de criacao/alteracao de um componente.
 */
@Schema(name = "ComponenteRequest", description = "Componente utilizado por um sistema")
public record ComponenteRequest(

        @Schema(description = "Nome do componente", example = "Autenticacao SSO")
        @NotBlank(message = "O nome do componente e obrigatorio")
        @Size(max = 50, message = "O nome deve ter no maximo 50 caracteres")
        String nome,

        @Schema(description = "Funcionalidade do componente")
        @NotBlank(message = "A descricao do componente e obrigatoria")
        @Size(max = 600, message = "A descricao deve ter no maximo 600 caracteres")
        String descricao,

        @Schema(description = "Registro ativo", defaultValue = "true")
        Boolean ativo) {

    public boolean registroAtivo() {
        return ativo == null || ativo;
    }
}