package br.gov.anvisa.portfolio.domain.backup;

import br.gov.anvisa.portfolio.domain.shared.PaginaRequest;
import br.gov.anvisa.portfolio.domain.shared.Textos;

/**
 * Filtro de consulta do agregado Backup.
 *
 * @param servidorId  codigo do servidor (relacionamento RL_SERVIDOR_BACKUP)
 * @param tipoBackup  tipo do backup (Commvault: Full, Incremental, Transaction Log)
 * @param instancia   instancia do Commvault
 * @param termo       pesquisa livre (caminho ou script)
 * @param paginacao   paginacao e ordenacao
 */
public record BackupFiltro(Long servidorId,
                           String tipoBackup,
                           String instancia,
                           String termo,
                           PaginaRequest paginacao) {

    public BackupFiltro {
        String normalizado = Textos.normalizar(termo);
        termo = normalizado == null || normalizado.isEmpty() ? null : normalizado;
        paginacao = paginacao == null ? PaginaRequest.padrao("id") : paginacao;
    }

    public static BackupFiltro padrao(PaginaRequest paginacao) {
        return new BackupFiltro(null, null, null, null, paginacao);
    }
}