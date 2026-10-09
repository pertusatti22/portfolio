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
 * Entidade de persistencia da tabela TB_ROTINA.
 */
@Entity
@Table(name = "TB_ROTINA")
@Getter
@Setter
public class RotinaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenciaRotina")
    @SequenceGenerator(name = "sequenciaRotina", sequenceName = "SQ_COSEQROTINA", allocationSize = 1)
    @Column(name = "CO_SEQ_ROTINA")
    private Long id;

    @Column(name = "CO_SCHEMA", nullable = false)
    private Long schemaId;

    @Column(name = "DS_OWNER", nullable = false, length = 50)
    private String owner;

    @Column(name = "DS_JOB_NAME", nullable = false, length = 50)
    private String nomeJob;

    @Column(name = "DS_REPEAT_INTERVAL", nullable = false, length = 100)
    private String intervaloExecucao;

    @Column(name = "ST_REGISTRO_ATIVO", length = 1)
    private String registroAtivo;
}