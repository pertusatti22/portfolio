package br.gov.anvisa.portfolio.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.gov.anvisa.portfolio.infrastructure.persistence.entity.DocumentacaoEntity;

/**
 * Repositorio Spring Data da tabela TB_DOCUMENTACAO.
 */
public interface DocumentacaoJpaRepository extends JpaRepository<DocumentacaoEntity, Long>,
        JpaSpecificationExecutor<DocumentacaoEntity> {

    long countBySistemaIdAndRegistroAtivo(Long sistemaId, String registroAtivo);
}