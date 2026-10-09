package br.gov.anvisa.portfolio.application.backup;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.anvisa.portfolio.application.backup.dto.BackupRequest;
import br.gov.anvisa.portfolio.application.backup.dto.BackupResponse;
import br.gov.anvisa.portfolio.domain.backup.Backup;
import br.gov.anvisa.portfolio.domain.backup.BackupFiltro;
import br.gov.anvisa.portfolio.domain.backup.BackupPort;
import br.gov.anvisa.portfolio.domain.shared.Pagina;
import br.gov.anvisa.portfolio.domain.shared.RecursoNaoEncontradoException;
import br.gov.anvisa.portfolio.domain.shared.RegraDeNegocioException;
import br.gov.anvisa.portfolio.domain.vinculo.TipoVinculo;
import br.gov.anvisa.portfolio.domain.vinculo.VinculoPort;

/**
 * Casos de uso do agregado Backup.
 */
@Service
public class BackupService {

    private final BackupPort backupPort;
    private final VinculoPort vinculoPort;

    public BackupService(BackupPort backupPort, VinculoPort vinculoPort) {
        this.backupPort = backupPort;
        this.vinculoPort = vinculoPort;
    }

    @Transactional(readOnly = true)
    public Pagina<BackupResponse> listar(BackupFiltro filtro) {
        return backupPort.listar(filtro).map(this::paraResponse);
    }

    @Transactional(readOnly = true)
    public BackupResponse buscar(Long id) {
        return paraResponse(obterObrigatorio(id));
    }

    @Transactional
    public BackupResponse criar(BackupRequest request) {
        return paraResponse(backupPort.inserir(paraDominio(null, request)));
    }

    @Transactional
    public BackupResponse alterar(Long id, BackupRequest request) {
        obterObrigatorio(id);
        return paraResponse(backupPort.alterar(paraDominio(id, request)));
    }

    @Transactional
    public void excluir(Long id) {
        obterObrigatorio(id);
        vinculoPort.desvincularDonos(id, TipoVinculo.BACKUP);
        backupPort.excluir(id);
    }

    public Backup obterObrigatorio(Long id) {
        return backupPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Backup", id));
    }

    BackupResponse paraResponse(Backup backup) {
        return new BackupResponse(backup.id(),
                backup.frequencia(),
                backup.horario(),
                backup.caminho(),
                backup.scriptJob(),
                backup.instancia(),
                backup.tipoInstancia(),
                backup.conteudoBackup(),
                backup.data(),
                backup.tamanhoFullSemanalGb(),
                backup.tempoFullSemanal(),
                backup.frequenciaIncremental(),
                backup.horarioIncremental(),
                backup.tempoIncremental(),
                backup.archiveLog(),
                backup.tempoArchiveLog(),
                backup.eliminaArchiveLog(),
                backup.retencao(),
                backup.retencaoIncremental(),
                backup.retencaoArchiveLog(),
                backup.appType(),
                backup.instance(),
                backup.subclient(),
                backup.backupType(),
                backup.storagePolicy(),
                vinculoPort.listarDonos(backup.id(), TipoVinculo.BACKUP));
    }

    private Backup paraDominio(Long id, BackupRequest request) {
        return new Backup(id,
                request.frequencia(),
                request.horario(),
                request.caminho(),
                request.scriptJob(),
                request.instancia(),
                request.tipoInstancia(),
                request.conteudoBackup(),
                request.data(),
                tamanho(request.tamanhoFullSemanalGb()),
                request.tempoFullSemanal(),
                request.frequenciaIncremental(),
                request.horarioIncremental(),
                request.tempoIncremental(),
                request.archiveLog(),
                request.tempoArchiveLog(),
                request.eliminaArchiveLog(),
                request.retencao(),
                request.retencaoIncremental(),
                request.retencaoArchiveLog(),
                request.appType(),
                request.instance(),
                request.subclient(),
                request.backupType(),
                request.storagePolicy());
    }

    private BigDecimal tamanho(BigDecimal valor) {
        if (valor == null) {
            return null;
        }
        if (valor.signum() < 0) {
            throw new RegraDeNegocioException("O tamanho do backup full semanal nao pode ser negativo");
        }
        return valor;
    }
}