package br.gov.anvisa.portfolio.application.documentacao.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Representacao de uma documentacao.
 */
@Schema(name = "DocumentacaoResponse", description = "Documentacao de um sistema")
public record DocumentacaoResponse(Long id,
                                   Long sistemaId,
                                   String urlRepositorio,
                                   Long tipoDocumentacaoId,
                                   String tipoDocumentacao,
                                   boolean registroAtivo) {
}