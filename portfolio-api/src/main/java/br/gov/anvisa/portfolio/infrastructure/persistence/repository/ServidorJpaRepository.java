package br.gov.anvisa.portfolio.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.gov.anvisa.portfolio.infrastructure.persistence.entity.ServidorEntity;

/**
 * Repositorio Spring Data da tabela TB_SERVIDOR.
 */
public interface ServidorJpaRepository extends JpaRepository<ServidorEntity, Long>,
        JpaSpecificationExecutor<ServidorEntity> {

    Optional<ServidorEntity> findFirstByNomeIgnoreCase(String nome);
}