package br.gov.anvisa.portfolio.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.gov.anvisa.portfolio.infrastructure.persistence.entity.RotinaEntity;

/**
 * Repositorio Spring Data da tabela TB_ROTINA.
 */
public interface RotinaJpaRepository extends JpaRepository<RotinaEntity, Long>,
        JpaSpecificationExecutor<RotinaEntity> {

    Optional<RotinaEntity> findByOwnerAndNomeJobIgnoreCase(String owner, String nomeJob);
}