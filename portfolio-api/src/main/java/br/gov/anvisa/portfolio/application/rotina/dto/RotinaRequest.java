package br.gov.anvisa.portfolio.application.rotina.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Payload de criacao/alteracao de uma rotina agendada.
 */
@Schema(name = "RotinaRequest", description = "Rotina agendada no banco de dados")
public record RotinaRequest(

        @Schema(description = "Codigo do catalogo de schema")
        @NotNull(message = "O schema e obrigatorio")
        Long schemaId,

        @Schema(description = "Usuario dono do agendamento", example = "SGAS")
        @NotBlank(message = "O owner e obrigatorio")
        @Size(max = 50, message = "O owner deve ter no maximo 50 caracteres")
        String owner,

        @Schema(description = "Nome do job", example = "JOB_ENVIO_EMAIL")
        @NotBlank(message = "O nome do job e obrigatorio")
        @Size(max = 50, message = "O nome do job deve ter no maximo 50 caracteres")
        String nomeJob,

        @Schema(description = "Frequencia de execucao", example = "FREQ=DAILY;BYHOUR=2")
        @NotBlank(message = "A frequencia de execucao e obrigatoria")
        @Size(max = 100, message = "A frequencia deve ter no maximo 100 caracteres")
        String intervaloExecucao,

        @Schema(description = "Registro ativo", defaultValue = "true")
        Boolean ativo) {

    public boolean registroAtivo() {
        return ativo == null || ativo;
    }
}