package br.gov.anvisa.portfolio.application.usuario.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Payload de criacao/alteracao de um usuario de banco de dados.
 */
@Schema(name = "UsuarioRequest", description = "Usuario interno vinculado a um usuario de banco de dados")
public record UsuarioRequest(

        @Schema(description = "Login do usuario", example = "JSILVA")
        @NotBlank(message = "O usuario e obrigatorio")
        @Size(max = 30, message = "O usuario deve ter no maximo 30 caracteres")
        String username,

        @Schema(description = "Nome da maquina do usuario", example = "NOTE-JSILVA")
        @NotBlank(message = "O host e obrigatorio")
        @Size(max = 100, message = "O host deve ter no maximo 100 caracteres")
        String host,

        @Schema(description = "Codigo do catalogo de tipo de usuario")
        @NotNull(message = "O tipo de usuario e obrigatorio")
        Long tipoUsuarioId,

        @Schema(description = "Data de inicio de vigencia", example = "2024-01-01")
        @NotNull(message = "A data de inicio de vigencia e obrigatoria")
        LocalDate inicioVigencia,

        @Schema(description = "Data de fim de vigencia (nulo para usuario ativo)", example = "2024-12-31")
        LocalDate fimVigencia,

        @Schema(description = "Codigo do usuario de banco de dados")
        @NotNull(message = "O usuario de banco de dados e obrigatorio")
        Long usuarioBancoDadosId,

        @Schema(description = "Usuario secundario (campo legado)")
        @Size(max = 30)
        String usuarioSecundario) {
}