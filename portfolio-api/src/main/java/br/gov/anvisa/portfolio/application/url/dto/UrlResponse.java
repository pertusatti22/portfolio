package br.gov.anvisa.portfolio.application.url.dto;

import br.gov.anvisa.portfolio.domain.url.TipoUrl;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Representacao de uma URL.
 */
@Schema(name = "UrlResponse", description = "URL de acesso a sistema ou componente")
public record UrlResponse(Long id,
                          String endereco,
                          TipoUrl tipo,
                          String tipoRotulo,
                          Long ambienteId,
                          String ambiente,
                          Long acessoId,
                          String acesso,
                          Long frameworkId,
                          String framework,
                          Long autenticacaoId,
                          String autenticacao,
                          boolean registroAtivo) {
}