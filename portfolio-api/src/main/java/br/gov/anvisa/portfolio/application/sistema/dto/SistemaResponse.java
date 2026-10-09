package br.gov.anvisa.portfolio.application.sistema.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Representacao de um sistema na listagem.
 */
@Schema(name = "SistemaResponse", description = "Sistema do portifolio")
public record SistemaResponse(Long id,
                              String sigla,
                              String nome,
                              Long origemId,
                              String origem,
                              String objetivo,
                              String arquivo,
                              boolean anvisa,
                              boolean integracaoContinua,
                              String classificacaoBem,
                              Long baseDeDadoId,
                              String baseDeDado,
                              boolean registroAtivo,
                              boolean validado) {
}