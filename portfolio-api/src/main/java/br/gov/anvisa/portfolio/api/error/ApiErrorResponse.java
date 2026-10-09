package br.gov.anvisa.portfolio.api.error;

import java.time.Instant;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Corpo padronizado das respostas de erro da API.
 *
 * @param timestamp momento da ocorrencia
 * @param status    codigo HTTP
 * @param erro      familia do erro (bad_request, not_found, conflict, validation)
 * @param mensagem  descricao legivel do erro
 * @param caminho   recurso requisitado
 * @param campos    erros de validacao por campo (quando aplicavel)
 */
@Schema(name = "ApiErrorResponse", description = "Erro retornado pela API")
public record ApiErrorResponse(Instant timestamp,
                               int status,
                               String erro,
                               String mensagem,
                               String caminho,
                               List<CampoErro> campos) {

    public record CampoErro(String campo, String mensagem) {
    }
}