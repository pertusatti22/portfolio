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
 * Entidade de persistencia da tabela TB_DOCUMENTACAO.
 */
@Entity
@Table(name = "TB_DOCUMENTACAO")
@Getter
@Setter
public class DocumentacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenciaDocumentacao")
    @SequenceGenerator(name = "sequenciaDocumentacao", sequenceName = "SQ_COSEQDOCUMENTACAO", allocationSize = 1)
    @Column(name = "CO_SEQ_DOCUMENTACAO")
    private Long id;

    @Column(name = "CO_SISTEMA", nullable = false)
    private Long sistemaId;

    @Column(name = "DS_URL_REPOSITORIO", nullable = false, length = 600)
    private String urlRepositorio;

    @Column(name = "CO_TIPO_DOCUMENTACAO", nullable = false)
    private Long tipoDocumentacaoId;

    @Column(name = "ST_REGISTRO_ATIVO", length = 1)
    private String registroAtivo;
}