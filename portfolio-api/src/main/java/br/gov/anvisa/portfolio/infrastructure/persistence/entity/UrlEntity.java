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
 * Entidade de persistencia da tabela TB_URL.
 */
@Entity
@Table(name = "TB_URL")
@Getter
@Setter
public class UrlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenciaUrl")
    @SequenceGenerator(name = "sequenciaUrl", sequenceName = "SQ_COSEQURL", allocationSize = 1)
    @Column(name = "CO_SEQ_URL")
    private Long id;

    @Column(name = "DS_URL", nullable = false, length = 255)
    private String endereco;

    @Column(name = "TP_URL", nullable = false, length = 1)
    private String tipo;

    @Column(name = "CO_AMBIENTE", nullable = false)
    private Long ambienteId;

    @Column(name = "CO_ACESSO", nullable = false)
    private Long acessoId;

    @Column(name = "CO_FRAMEWORK", nullable = false)
    private Long frameworkId;

    @Column(name = "CO_AUTENTICACAO", nullable = false)
    private Long autenticacaoId;

    @Column(name = "ST_REGISTRO_ATIVO", length = 1)
    private String registroAtivo;
}