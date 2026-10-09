package br.gov.anvisa.portfolio.application.servidor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.anvisa.portfolio.application.backup.BackupService;
import br.gov.anvisa.portfolio.application.backup.dto.BackupResponse;
import br.gov.anvisa.portfolio.application.catalogo.CatalogoService;
import br.gov.anvisa.portfolio.application.catalogo.dto.CatalogoResponse;
import br.gov.anvisa.portfolio.domain.catalogo.CatalogoTipo;
import br.gov.anvisa.portfolio.domain.servidor.ServidorPort;
import br.gov.anvisa.portfolio.domain.shared.RecursoNaoEncontradoException;
import br.gov.anvisa.portfolio.domain.vinculo.TipoVinculo;
import br.gov.anvisa.portfolio.domain.vinculo.VinculoPort;

/**
 * Casos de uso dos vinculos do servidor (bases de dados e rotinas de backup).
 */
@Service
public class ServidorVinculosService {

    private final VinculoPort vinculoPort;
    private final ServidorPort servidorPort;
    private final BackupService backupService;
    private final CatalogoService catalogoService;

    public ServidorVinculosService(VinculoPort vinculoPort,
                                   ServidorPort servidorPort,
                                   BackupService backupService,
                                   CatalogoService catalogoService) {
        this.vinculoPort = vinculoPort;
        this.servidorPort = servidorPort;
        this.backupService = backupService;
        this.catalogoService = catalogoService;
    }

    @Transactional(readOnly = true)
    public List<CatalogoResponse> listarBasesDeDado(Long servidorId) {
        garantirServidor(servidorId);
        return vinculoPort.listarVinculados(servidorId, TipoVinculo.BASE_DE_DADO).stream()
                .map(id -> catalogoService.buscar(CatalogoTipo.BASE_DE_DADO, id))
                .toList();
    }

    @Transactional
    public List<CatalogoResponse> substituirBasesDeDado(Long servidorId, List<Long> baseDeDadoIds) {
        baseDeDadoIds.forEach(id -> catalogoService.buscar(CatalogoTipo.BASE_DE_DADO, id));
        garantirServidor(servidorId);
        vinculoPort.substituirVinculos(servidorId, TipoVinculo.BASE_DE_DADO, baseDeDadoIds);
        return listarBasesDeDado(servidorId);
    }

    @Transactional(readOnly = true)
    public List<Long> listarBackups(Long servidorId) {
        garantirServidor(servidorId);
        return vinculoPort.listarVinculados(servidorId, TipoVinculo.BACKUP);
    }

    @Transactional(readOnly = true)
    public List<BackupResponse> listarBackupsDetalhado(Long servidorId) {
        return listarBackups(servidorId).stream()
                .map(backupService::buscar)
                .toList();
    }

    @Transactional
    public List<Long> substituirBackups(Long servidorId, List<Long> backupIds) {
        backupIds.forEach(id -> backupService.obterObrigatorio(id));
        garantirServidor(servidorId);
        vinculoPort.substituirVinculos(servidorId, TipoVinculo.BACKUP, backupIds);
        return listarBackups(servidorId);
    }

    private void garantirServidor(Long servidorId) {
        if (servidorPort.buscarPorId(servidorId).isEmpty()) {
            throw new RecursoNaoEncontradoException("Servidor", servidorId);
        }
    }
}