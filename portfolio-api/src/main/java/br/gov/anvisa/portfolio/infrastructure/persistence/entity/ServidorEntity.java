package br.gov.anvisa.portfolio.infrastructure.persistence.entity;

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
 * Entidade de persistencia da tabela TB_SERVIDOR.
 *
 * <p>Os atributos quantitativos permanecem como texto, conforme o DDL legado (VARCHAR2).
 */
@Entity
@Table(name = "TB_SERVIDOR")
@Getter
@Setter
public class ServidorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenciaServidor")
    @SequenceGenerator(name = "sequenciaServidor", sequenceName = "SQ_COSEQSERVIDOR", allocationSize = 1)
    @Column(name = "CO_SEQ_SERVIDOR")
    private Long id;

    @Column(name = "NO_SERVIDOR", length = 50)
    private String nome;

    @Column(name = "ST_LIGADO", length = 1)
    private String ligado;

    @Column(name = "QT_CPU", length = 50)
    private String cpu;

    @Column(name = "QT_MEMORIA_GB", length = 50)
    private String memoriaGb;

    @Column(name = "CO_SIST_OPER")
    private Long sistemaOperacionalId;

    @Column(name = "QT_TAMANHO_GB", length = 50)
    private String tamanhoGb;

    @Column(name = "CO_ARMAZENAMENTO")
    private Long armazenamentoId;

    @Column(name = "CO_AMBIENTE")
    private Long ambienteId;

    @Column(name = "DS_APLICACAO", length = 100)
    private String aplicacao;

    @Column(name = "ST_BACKUP", length = 1)
    private String backup;
}