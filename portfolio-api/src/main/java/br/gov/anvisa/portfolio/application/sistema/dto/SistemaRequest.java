package br.gov.anvisa.portfolio.application.sistema.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload de criacao/alteracao de um sistema.
 */
@Schema(name = "SistemaRequest", description = "Dados cadastrais de um sistema")
public record SistemaRequest(

        @Schema(description = "Sigla do sistema", example = "SGAS")
        @NotBlank(message = "A sigla e obrigatoria")
        @Size(max = 50, message = "A sigla deve ter no maximo 50 caracteres")
        String sigla,

        @Schema(description = "Nome exibido na tela do sistema", example = "Sistema de Gestao de Autorizacoes")
        @NotBlank(message = "O nome e obrigatorio")
        @Size(max = 150, message = "O nome deve ter no maximo 150 caracteres")
        String nome,

        @Schema(description = "Codigo do catalogo de origem")
        Long origemId,

        @Schema(description = "Objetivo do sistema")
        @Size(max = 1000, message = "O objetivo deve ter no maximo 1000 caracteres")
        String objetivo,

        @Schema(description = "Nome do arquivo a ser alterado no banco, caso externo")
        @Size(max = 150, message = "O arquivo deve ter no maximo 150 caracteres")
        String arquivo,

        @Schema(description = "Sistema desenvolvido na Anvisa", defaultValue = "true")
        Boolean anvisa,

        @Schema(description = "Sistema no projeto de integracao continua", defaultValue = "false")
        Boolean integracaoContinua,

        @Schema(description = "Classificacao do bem / vida util")
        @Size(max = 100, message = "A classificacao deve ter no maximo 100 caracteres")
        String classificacaoBem,

        @Schema(description = "Codigo do catalogo de base de dados")
        Long baseDeDadoId,

        @Schema(description = "Registro ativo", defaultValue = "true")
        Boolean ativo,

        @Schema(description = "Dados validados pelo responsavel", defaultValue = "false")
        Boolean validado) {

    public Boolean anvisa() {
        return anvisa == null || anvisa;
    }

    public Boolean integracaoContinua() {
        return integracaoContinua != null && integracaoContinua;
    }

    public boolean registroAtivo() {
        return ativo == null || ativo;
    }

    public Boolean validado() {
        return validado != null && validado;
    }
}