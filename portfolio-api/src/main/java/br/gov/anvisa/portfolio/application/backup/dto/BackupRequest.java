package br.gov.anvisa.portfolio.application.backup.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Payload de criacao/alteracao de uma rotina de backup.
 */
@Schema(name = "BackupRequest", description = "Rotina de backup de um servidor")
public record BackupRequest(

        @Schema(description = "Frequencia do backup", example = "Diario")
        String frequencia,

        @Schema(description = "Horario de execucao", example = "02:00")
        String horario,

        @Schema(description = "Caminho da rotina de backup", example = "/backup/prod/full")
        String caminho,

        @Schema(description = "Script de execucao do backup")
        String scriptJob,

        @Schema(description = "Nome da instancia", example = "VS01")
        String instancia,

        @Schema(description = "Tipo de instancia", example = "Single Instance")
        String tipoInstancia,

        @Schema(description = "Conteudo contemplado pelo backup")
        String conteudoBackup,

        @Schema(description = "Dia de execucao")
        String data,

        @Schema(description = "Tamanho do backup full semanal em GB", example = "2048.00")
        BigDecimal tamanhoFullSemanalGb,

        @Schema(description = "Tempo de execucao do backup full semanal", example = "01:45")
        String tempoFullSemanal,

        @Schema(description = "Frequencia do backup incremental", example = "Mensal")
        String frequenciaIncremental,

        @Schema(description = "Horario do backup incremental", example = "02:30")
        String horarioIncremental,

        @Schema(description = "Tempo do backup incremental", example = "00:40")
        String tempoIncremental,

        @Schema(description = "Conteudo do archive log", example = "FS")
        String archiveLog,

        @Schema(description = "Tempo do archive log", example = "00:15")
        String tempoArchiveLog,

        @Schema(description = "Elimina o archive log", defaultValue = "false")
        Boolean eliminaArchiveLog,

        @Schema(description = "Retencao do backup", example = "30 dias")
        String retencao,

        @Schema(description = "Retencao do backup incremental")
        String retencaoIncremental,

        @Schema(description = "Retencao do archive log")
        String retencaoArchiveLog,

        @Schema(description = "Tipo de aplicacao (Commvault)", example = "Virtual Server")
        String appType,

        @Schema(description = "Instancia (Commvault)", example = "VMInstance")
        String instance,

        @Schema(description = "Subcliente (Commvault)", example = "Banco de dados")
        String subclient,

        @Schema(description = "Tipo de backup (Commvault)", example = "Full")
        String backupType,

        @Schema(description = "Politica de armazenamento (Commvault)", example = "POL_RET_30")
        String storagePolicy) {

    public Boolean eliminaArchiveLog() {
        return eliminaArchiveLog != null && eliminaArchiveLog;
    }
}