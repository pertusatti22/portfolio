package br.gov.anvisa.portfolio.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.gov.anvisa.portfolio.infrastructure.persistence.entity.UsuarioEntity;

/**
 * Repositorio Spring Data da tabela TB_USUARIO.
 */
public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, String>,
        JpaSpecificationExecutor<UsuarioEntity> {
}