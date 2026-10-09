package br.gov.anvisa.portfolio.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.gov.anvisa.portfolio.infrastructure.persistence.entity.ComponenteEntity;

/**
 * Repositorio Spring Data da tabela TB_COMPONENTE.
 */
public interface ComponenteJpaRepository extends JpaRepository<ComponenteEntity, Long>,
        JpaSpecificationExecutor<ComponenteEntity> {

    long countBySistemaIdAndRegistroAtivo(Long sistemaId, String registroAtivo);
}