package br.gov.anvisa.portfolio.application.backup.dto;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Representacao de uma rotina de backup.
 */
@Schema(name = "BackupResponse", description = "Rotina de backup de um servidor")
public record BackupResponse(Long id,
                             String frequencia,
                             String horario,
                             String caminho,
                             String scriptJob,
                             String instancia,
                             String tipoInstancia,
                             String conteudoBackup,
                             String data,
                             BigDecimal tamanhoFullSemanalGb,
                             String tempoFullSemanal,
                             String frequenciaIncremental,
                             String horarioIncremental,
                             String tempoIncremental,
                             String archiveLog,
                             String tempoArchiveLog,
                             boolean eliminaArchiveLog,
                             String retencao,
                             String retencaoIncremental,
                             String retencaoArchiveLog,
                             String appType,
                             String instance,
                             String subclient,
                             String backupType,
                             String storagePolicy,
                             List<Long> servidoresIds) {
}