package br.gov.anvisa.portfolio.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.gov.anvisa.portfolio.infrastructure.persistence.entity.SistemaEntity;

/**
 * Repositorio Spring Data da tabela TB_SISTEMA.
 */
public interface SistemaJpaRepository extends JpaRepository<SistemaEntity, Long>,
        JpaSpecificationExecutor<SistemaEntity> {

    boolean existsBySiglaIgnoreCase(String sigla);
}