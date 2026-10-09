package br.gov.anvisa.portfolio.domain.backup;

import java.math.BigDecimal;

import br.gov.anvisa.portfolio.domain.shared.Textos;

/**
 * Agregado Backup: definicao de rotina de backup/restore vinculada a um servidor.
 *
 * <p>Por compatibilidade com o DDL legado, os atributos sao mantidos como texto, exceto
 * {@code tamanhoFullSemanalGb} (NUMBER(10,2)).
 */
public record Backup(Long id,
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
                     String storagePolicy) {

    public Backup {
        frequencia = Textos.opcional(frequencia, 50);
        horario = Textos.opcional(horario, 50);
        caminho = Textos.opcional(caminho, 100);
        scriptJob = Textos.opcional(scriptJob, 100);
        instancia = Textos.opcional(instancia, 25);
        tipoInstancia = Textos.opcional(tipoInstancia, 25);
        conteudoBackup = Textos.opcional(conteudoBackup, 100);
        data = Textos.opcional(data, 25);
        tempoFullSemanal = Textos.opcional(tempoFullSemanal, 20);
        frequenciaIncremental = Textos.opcional(frequenciaIncremental, 15);
        horarioIncremental = Textos.opcional(horarioIncremental, 20);
        tempoIncremental = Textos.opcional(tempoIncremental, 20);
        archiveLog = Textos.opcional(archiveLog, 25);
        tempoArchiveLog = Textos.opcional(tempoArchiveLog, 20);
        retencao = Textos.opcional(retencao, 25);
        retencaoIncremental = Textos.opcional(retencaoIncremental, 25);
        retencaoArchiveLog = Textos.opcional(retencaoArchiveLog, 25);
        appType = Textos.opcional(appType, 50);
        instance = Textos.opcional(instance, 50);
        subclient = Textos.opcional(subclient, 50);
        backupType = Textos.opcional(backupType, 50);
        storagePolicy = Textos.opcional(storagePolicy, 100);
    }

    public Backup comId(Long novoId) {
        return new Backup(novoId, frequencia, horario, caminho, scriptJob, instancia, tipoInstancia,
                conteudoBackup, data, tamanhoFullSemanalGb, tempoFullSemanal, frequenciaIncremental,
                horarioIncremental, tempoIncremental, archiveLog, tempoArchiveLog, eliminaArchiveLog,
                retencao, retencaoIncremental, retencaoArchiveLog, appType, instance, subclient,
                backupType, storagePolicy);
    }
}