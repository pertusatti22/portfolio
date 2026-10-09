package br.gov.anvisa.portfolio.infrastructure.persistence.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.anvisa.portfolio.domain.backup.Backup;
import br.gov.anvisa.portfolio.domain.backup.BackupFiltro;
import br.gov.anvisa.portfolio.domain.backup.BackupPort;
import br.gov.anvisa.portfolio.domain.shared.Pagina;
import br.gov.anvisa.portfolio.domain.vinculo.TipoVinculo;
import br.gov.anvisa.portfolio.domain.vinculo.VinculoPort;
import br.gov.anvisa.portfolio.infrastructure.persistence.entity.BackupEntity;
import br.gov.anvisa.portfolio.infrastructure.persistence.repository.BackupJpaRepository;
import br.gov.anvisa.portfolio.infrastructure.persistence.support.Indicadores;
import br.gov.anvisa.portfolio.infrastructure.persistence.support.Paginas;
import jakarta.persistence.criteria.Predicate;

/**
 * Adaptador JPA da porta {@link BackupPort}.
 *
 * <p>O filtro por servidor utiliza a tabela de relacionamento RL_SERVIDOR_BACKUP.
 */
@Repository
public class BackupJpaAdapter implements BackupPort {

    private static final Set<String> COLUNAS_ORDENACAO =
            Set.of("id", "frequencia", "backupType", "instancia", "caminho");

    private final BackupJpaRepository repositorio;
    private final VinculoPort vinculoPort;

    public BackupJpaAdapter(BackupJpaRepository repositorio, VinculoPort vinculoPort) {
        this.repositorio = repositorio;
        this.vinculoPort = vinculoPort;
    }

    @Override
    @Transactional(readOnly = true)
    public Pagina<Backup> listar(BackupFiltro filtro) {
        Pageable pageable = Paginas.para(filtro.paginacao(), COLUNAS_ORDENACAO, "id");
        Page<BackupEntity> pagina = repositorio.findAll(especificacao(filtro), pageable);
        List<Backup> itens = pagina.getContent().stream().map(this::paraDominio).toList();
        return Pagina.de(itens, pagina.getNumber(), pagina.getSize(), pagina.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Backup> buscarPorId(Long id) {
        return repositorio.findById(id).map(this::paraDominio);
    }

    @Override
    @Transactional
    public Backup inserir(Backup backup) {
        BackupEntity entity = new BackupEntity();
        aplicar(entity, backup);
        return paraDominio(repositorio.save(entity));
    }

    @Override
    @Transactional
    public Backup alterar(Backup backup) {
        BackupEntity entity = repositorio.findById(backup.id()).orElseThrow();
        aplicar(entity, backup);
        return paraDominio(repositorio.save(entity));
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        vinculoPort.desvincularDonos(id, TipoVinculo.BACKUP);
        repositorio.deleteById(id);
    }

    private Specification<BackupEntity> especificacao(BackupFiltro filtro) {
        return (raiz, consulta, criteriaBuilder) -> {
            List<Predicate> condicoes = new ArrayList<>();

            if (filtro.termo() != null) {
                String busca = "%" + filtro.termo().toUpperCase() + "%";
                condicoes.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.upper(raiz.get("caminho")), busca),
                        criteriaBuilder.like(criteriaBuilder.upper(raiz.get("scriptJob")), busca)));
            }
            if (filtro.tipoBackup() != null) {
                condicoes.add(criteriaBuilder.equal(raiz.get("backupType"), filtro.tipoBackup()));
            }
            if (filtro.instancia() != null) {
                condicoes.add(criteriaBuilder.equal(raiz.get("instance"), filtro.instancia()));
            }
            if (filtro.servidorId() != null) {
                List<Long> backupIds = vinculoPort.listarVinculados(filtro.servidorId(), TipoVinculo.BACKUP);
                if (backupIds.isEmpty()) {
                    condicoes.add(criteriaBuilder.disjunction());
                } else {
                    condicoes.add(raiz.get("id").in(backupIds));
                }
            }
            return condicoes.isEmpty() ? criteriaBuilder.conjunction()
                    : criteriaBuilder.and(condicoes.toArray(Predicate[]::new));
        };
    }

    private void aplicar(BackupEntity entity, Backup backup) {
        entity.setFrequencia(backup.frequencia());
        entity.setHorario(backup.horario());
        entity.setCaminho(backup.caminho());
        entity.setScriptJob(backup.scriptJob());
        entity.setInstancia(backup.instancia());
        entity.setTipoInstancia(backup.tipoInstancia());
        entity.setConteudoBackup(backup.conteudoBackup());
        entity.setData(backup.data());
        entity.setTamanhoFullSemanalGb(backup.tamanhoFullSemanalGb());
        entity.setTempoFullSemanal(backup.tempoFullSemanal());
        entity.setFrequenciaIncremental(backup.frequenciaIncremental());
        entity.setHorarioIncremental(backup.horarioIncremental());
        entity.setTempoIncremental(backup.tempoIncremental());
        entity.setArchiveLog(backup.archiveLog());
        entity.setTempoArchiveLog(backup.tempoArchiveLog());
        entity.setEliminaArchiveLog(Indicadores.de(backup.eliminaArchiveLog()));
        entity.setRetencao(backup.retencao());
        entity.setRetencaoIncremental(backup.retencaoIncremental());
        entity.setRetencaoArchiveLog(backup.retencaoArchiveLog());
        entity.setAppType(backup.appType());
        entity.setInstance(backup.instance());
        entity.setSubclient(backup.subclient());
        entity.setBackupType(backup.backupType());
        entity.setStoragePolicy(backup.storagePolicy());
    }

    private Backup paraDominio(BackupEntity entity) {
        return new Backup(entity.getId(),
                entity.getFrequencia(),
                entity.getHorario(),
                entity.getCaminho(),
                entity.getScriptJob(),
                entity.getInstancia(),
                entity.getTipoInstancia(),
                entity.getConteudoBackup(),
                entity.getData(),
                entity.getTamanhoFullSemanalGb(),
                entity.getTempoFullSemanal(),
                entity.getFrequenciaIncremental(),
                entity.getHorarioIncremental(),
                entity.getTempoIncremental(),
                entity.getArchiveLog(),
                entity.getTempoArchiveLog(),
                Indicadores.para(entity.getEliminaArchiveLog()),
                entity.getRetencao(),
                entity.getRetencaoIncremental(),
                entity.getRetencaoArchiveLog(),
                entity.getAppType(),
                entity.getInstance(),
                entity.getSubclient(),
                entity.getBackupType(),
                entity.getStoragePolicy());
    }
}