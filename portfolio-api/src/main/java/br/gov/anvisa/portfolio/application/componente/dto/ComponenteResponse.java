package br.gov.anvisa.portfolio.application.componente.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Representacao de um componente.
 */
@Schema(name = "ComponenteResponse", description = "Componente de um sistema")
public record ComponenteResponse(Long id,
                                 Long sistemaId,
                                 String nome,
                                 String descricao,
                                 boolean registroAtivo) {
}