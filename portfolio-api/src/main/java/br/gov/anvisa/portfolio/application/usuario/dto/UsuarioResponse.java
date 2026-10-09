package br.gov.anvisa.portfolio.application.usuario.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Representacao de um usuario.
 */
@Schema(name = "UsuarioResponse", description = "Usuario do portifolio")
public record UsuarioResponse(String username,
                              String host,
                              Long tipoUsuarioId,
                              String tipoUsuario,
                              LocalDate inicioVigencia,
                              LocalDate fimVigencia,
                              Long usuarioBancoDadosId,
                              String usuarioBancoDados,
                              String usuarioSecundario,
                              boolean vigente) {
}