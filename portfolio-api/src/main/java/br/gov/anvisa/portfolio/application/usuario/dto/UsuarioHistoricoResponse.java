package br.gov.anvisa.portfolio.application.usuario.dto;

import java.time.Instant;
import java.time.LocalDate;

import br.gov.anvisa.portfolio.domain.usuario.TipoOperacao;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Representacao de um registro do historico de vigencias de usuarios (TB_HIST_USUARIO).
 */
@Schema(name = "UsuarioHistoricoResponse", description = "Historico de vigencia de usuario")
public record UsuarioHistoricoResponse(String username,
                                       String host,
                                       Long tipoUsuarioId,
                                       String tipoUsuario,
                                       LocalDate inicioVigencia,
                                       LocalDate fimVigencia,
                                       Long usuarioBancoDadosId,
                                       String usuarioBancoDados,
                                       String usuarioAplicacao,
                                       Instant dataRegistro,
                                       TipoOperacao operacao) {
}