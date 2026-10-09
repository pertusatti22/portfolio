package br.gov.anvisa.portfolio.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.gov.anvisa.portfolio.infrastructure.persistence.entity.UrlEntity;

/**
 * Repositorio Spring Data da tabela TB_URL.
 */
public interface UrlJpaRepository extends JpaRepository<UrlEntity, Long>,
        JpaSpecificationExecutor<UrlEntity> {
}