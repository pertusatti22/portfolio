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
 * Entidade de persistencia da tabela TB_COMPONENTE.
 */
@Entity
@Table(name = "TB_COMPONENTE")
@Getter
@Setter
public class ComponenteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenciaComponente")
    @SequenceGenerator(name = "sequenciaComponente", sequenceName = "SQ_COSEQCOMPONENTE", allocationSize = 1)
    @Column(name = "CO_SEQ_COMPONENTE")
    private Long id;

    @Column(name = "CO_SISTEMA", nullable = false)
    private Long sistemaId;

    @Column(name = "NO_COMPONENTE", nullable = false, length = 50)
    private String nome;

    @Column(name = "DS_COMPONENTE", nullable = false, length = 600)
    private String descricao;

    @Column(name = "ST_REGISTRO_ATIVO", length = 1)
    private String registroAtivo;
}