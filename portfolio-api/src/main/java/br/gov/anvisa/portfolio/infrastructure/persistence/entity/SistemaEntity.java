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
 * Entidade de persistencia da tabela TB_SISTEMA.
 */
@Entity
@Table(name = "TB_SISTEMA")
@Getter
@Setter
public class SistemaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenciaSistema")
    @SequenceGenerator(name = "sequenciaSistema", sequenceName = "SQ_COSEQSISTEMA", allocationSize = 1)
    @Column(name = "CO_SEQ_SISTEMA")
    private Long id;

    @Column(name = "SG_SISTEMA", nullable = false, length = 50)
    private String sigla;

    @Column(name = "NO_SISTEMA", nullable = false, length = 150)
    private String nome;

    @Column(name = "CO_ORIGEM")
    private Long origemId;

    @Column(name = "DS_OBJETIVO", length = 1000)
    private String objetivo;

    @Column(name = "DS_ARQUIVO", length = 150)
    private String arquivo;

    @Column(name = "ST_ANVISA", length = 1)
    private String anvisa;

    @Column(name = "ST_INTEGRACAO_CONTINUA", length = 1)
    private String integracaoContinua;

    @Column(name = "DS_CLASSIFICACAO_BEM", length = 100)
    private String classificacaoBem;

    @Column(name = "CO_BASEDEDADOS")
    private Long baseDeDadoId;

    @Column(name = "ST_REGISTRO_ATIVO", length = 1)
    private String registroAtivo;

    @Column(name = "ST_VALIDADO", length = 1)
    private String validado;
}