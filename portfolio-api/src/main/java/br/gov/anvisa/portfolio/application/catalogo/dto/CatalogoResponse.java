package br.gov.anvisa.portfolio.application.catalogo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Representacao de um item de catalogo.
 */
@Schema(name = "CatalogoResponse", description = "Item de catalogo de dados de referencia")
public record CatalogoResponse(Long id,
                               String descricao,
                               String versao,
                               Boolean liberado,
                               boolean registroAtivo) {
}