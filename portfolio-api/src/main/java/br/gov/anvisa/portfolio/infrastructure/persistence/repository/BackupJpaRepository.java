package br.gov.anvisa.portfolio.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.gov.anvisa.portfolio.infrastructure.persistence.entity.BackupEntity;

/**
 * Repositorio Spring Data da tabela TB_BACKUP.
 */
public interface BackupJpaRepository extends JpaRepository<BackupEntity, Long>,
        JpaSpecificationExecutor<BackupEntity> {
}