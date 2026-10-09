package br.gov.anvisa.portfolio.infrastructure.persistence.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidade de persistencia da tabela TB_BACKUP.
 */
@Entity
@Table(name = "TB_BACKUP")
@Getter
@Setter
public class BackupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenciaBackup")
    @SequenceGenerator(name = "sequenciaBackup", sequenceName = "SQ_COSEQBACKUP", allocationSize = 1)
    @Column(name = "CO_SEQ_BACKUP")
    private Long id;

    @Column(name = "DS_FREQUENCIA", length = 50)
    private String frequencia;

    @Column(name = "DS_HORARIO", length = 50)
    private String horario;

    @Column(name = "DS_PATH", length = 100)
    private String caminho;

    @Column(name = "DS_SCRIPT_JOB", length = 100)
    private String scriptJob;

    @Column(name = "NO_INSTANCIA", length = 25)
    private String instancia;

    @Column(name = "DS_INSTANCIA", length = 25)
    private String tipoInstancia;

    @Column(name = "DS_CONTEUDO_BACKUP", length = 100)
    private String conteudoBackup;

    @Column(name = "DS_DATA", length = 25)
    private String data;

    @Column(name = "DS_TAMANHO_FS_GB", precision = 10, scale = 2)
    private BigDecimal tamanhoFullSemanalGb;

    @Column(name = "DS_TEMPO_FS", length = 20)
    private String tempoFullSemanal;

    @Column(name = "DS_FREQUENCIA_INC", length = 15)
    private String frequenciaIncremental;

    @Column(name = "DS_HORARIO_INC", length = 20)
    private String horarioIncremental;

    @Column(name = "DS_TEMPO_INC", length = 20)
    private String tempoIncremental;

    @Column(name = "DS_ARCHIVE_LOG", length = 25)
    private String archiveLog;

    @Column(name = "DS_TEMPO_AL", length = 20)
    private String tempoArchiveLog;

    @Column(name = "ST_DELETE_AL", length = 1)
    private String eliminaArchiveLog;

    @Column(name = "DS_RETENCAO", length = 25)
    private String retencao;

    @Column(name = "DS_RETENCAO_INC", length = 25)
    private String retencaoIncremental;

    @Column(name = "DS_RETENCAO_AL", length = 25)
    private String retencaoArchiveLog;

    @Column(name = "DS_APP_TYPE", length = 50)
    private String appType;

    @Column(name = "DS_INSTANCE", length = 50)
    private String instance;

    @Column(name = "DS_SUBCLIENT", length = 50)
    private String subclient;

    @Column(name = "DS_BACKUP_TYPE", length = 50)
    private String backupType;

    @Column(name = "DS_STORAGE_POLICY", length = 100)
    private String storagePolicy;
}