package br.gov.anvisa.portfolio.domain.backup;

import java.util.Optional;

import br.gov.anvisa.portfolio.domain.shared.Pagina;

/**
 * Porta de saida para o agregado Backup.
 */
public interface BackupPort {

    Pagina<Backup> listar(BackupFiltro filtro);

    Optional<Backup> buscarPorId(Long id);

    Backup inserir(Backup backup);

    Backup alterar(Backup backup);

    void excluir(Long id);
}