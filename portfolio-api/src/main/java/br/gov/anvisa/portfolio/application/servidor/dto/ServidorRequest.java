package br.gov.anvisa.portfolio.application.servidor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload de criacao/alteracao de um servidor.
 */
@Schema(name = "ServidorRequest", description = "Dados de um servidor do portifolio")
public record ServidorRequest(

        @Schema(description = "Nome do host", example = "HVLANV204")
        @NotBlank(message = "O nome do servidor e obrigatorio")
        @Size(max = 50, message = "O nome deve ter no maximo 50 caracteres")
        String nome,

        @Schema(description = "Servidor ligado", defaultValue = "true")
        Boolean ligado,

        @Schema(description = "Quantidade de CPU", example = "4")
        @Size(max = 50)
        String cpu,

        @Schema(description = "Quantidade de memoria em GB", example = "128")
        @Size(max = 50)
        String memoriaGb,

        @Schema(description = "Codigo do catalogo de sistema operacional")
        Long sistemaOperacionalId,

        @Schema(description = "Tamanho fisico em GB", example = "15854")
        @Size(max = 50)
        String tamanhoGb,

        @Schema(description = "Codigo do catalogo de armazenamento")
        Long armazenamentoId,

        @Schema(description = "Codigo do catalogo de ambiente")
        Long ambienteId,

        @Schema(description = "Aplicacoes hospedadas no servidor")
        @Size(max = 100, message = "A aplicacao deve ter no maximo 100 caracteres")
        String aplicacao) {

    public Boolean ligado() {
        return ligado != null && ligado;
    }
}