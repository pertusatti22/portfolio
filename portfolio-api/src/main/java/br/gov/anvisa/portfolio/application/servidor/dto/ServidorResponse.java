package br.gov.anvisa.portfolio.application.servidor.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Representacao de um servidor.
 */
@Schema(name = "ServidorResponse", description = "Servidor do portifolio")
public record ServidorResponse(Long id,
                               String nome,
                               boolean ligado,
                               String cpu,
                               String memoriaGb,
                               Long sistemaOperacionalId,
                               String sistemaOperacional,
                               String tamanhoGb,
                               Long armazenamentoId,
                               String armazenamento,
                               Long ambienteId,
                               String ambiente,
                               String aplicacao,
                               boolean possuiBackup) {
}