package br.gov.anvisa.portfolio.application.url.dto;

import br.gov.anvisa.portfolio.domain.url.TipoUrl;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Payload de criacao/alteracao de uma URL.
 */
@Schema(name = "UrlRequest", description = "URL de acesso a sistema ou componente")
public record UrlRequest(

        @Schema(description = "Endereco da URL", example = "https://homer.example.gov.br")
        @NotBlank(message = "A URL e obrigatoria")
        @Size(max = 255, message = "A URL deve ter no maximo 255 caracteres")
        String endereco,

        @Schema(description = "Tipo de ambiente da URL: PRODUCAO, RELEASE, HOMOLOGACAO ou DESENVOLVIMENTO",
                defaultValue = "DESENVOLVIMENTO")
        TipoUrl tipo,

        @Schema(description = "Codigo do catalogo de ambiente")
        @NotNull(message = "O ambiente e obrigatorio")
        Long ambienteId,

        @Schema(description = "Codigo do catalogo de acesso")
        @NotNull(message = "O tipo de acesso e obrigatorio")
        Long acessoId,

        @Schema(description = "Codigo do catalogo de framework")
        @NotNull(message = "O framework e obrigatorio")
        Long frameworkId,

        @Schema(description = "Codigo do catalogo de autenticacao")
        @NotNull(message = "A autenticacao e obrigatoria")
        Long autenticacaoId,

        @Schema(description = "Registro ativo", defaultValue = "true")
        Boolean ativo) {

    public TipoUrl tipo() {
        return tipo == null ? TipoUrl.padrao() : tipo;
    }

    public boolean registroAtivo() {
        return ativo == null || ativo;
    }
}