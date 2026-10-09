package br.gov.anvisa.portfolio.domain.usuario;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Registro do historico de vigencias (TB_HIST_USUARIO), alimentado por trigger do banco.
 *
 * @param username             nome de login
 * @param host                 nome do host
 * @param tipoUsuarioId        codigo do tipo de usuario
 * @param inicioVigencia       inicio da vigencia
 * @param fimVigencia          fim da vigencia, quando houver
 * @param usuarioBancoDadosId  codigo do usuario de banco de dados
 * @param usuarioAplicacao     nome do usuario da aplicacao
 * @param dataRegistro         momento do registro
 * @param operacao             tipo de operacao
 */
public record UsuarioHistorico(String username,
                               String host,
                               Long tipoUsuarioId,
                               LocalDate inicioVigencia,
                               LocalDate fimVigencia,
                               Long usuarioBancoDadosId,
                               String usuarioAplicacao,
                               Instant dataRegistro,
                               TipoOperacao operacao) {
}