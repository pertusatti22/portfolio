package br.gov.anvisa.portfolio.application.rotina.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Representacao de uma rotina agendada.
 */
@Schema(name = "RotinaResponse", description = "Rotina agendada no banco de dados")
public record RotinaResponse(Long id,
                             Long schemaId,
                             String schema,
                             String owner,
                             String nomeJob,
                             String intervaloExecucao,
                             boolean registroAtivo) {
}