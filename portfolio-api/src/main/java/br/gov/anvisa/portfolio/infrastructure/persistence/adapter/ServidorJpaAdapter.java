package br.gov.anvisa.portfolio.infrastructure.persistence.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.anvisa.portfolio.domain.servidor.Servidor;
import br.gov.anvisa.portfolio.domain.servidor.ServidorFiltro;
import br.gov.anvisa.portfolio.domain.servidor.ServidorPort;
import br.gov.anvisa.portfolio.domain.shared.Pagina;
import br.gov.anvisa.portfolio.domain.vinculo.TipoVinculo;
import br.gov.anvisa.portfolio.domain.vinculo.VinculoPort;
import br.gov.anvisa.portfolio.infrastructure.persistence.entity.ServidorEntity;
import br.gov.anvisa.portfolio.infrastructure.persistence.repository.ServidorJpaRepository;
import br.gov.anvisa.portfolio.infrastructure.persistence.support.Indicadores;
import br.gov.anvisa.portfolio.infrastructure.persistence.support.Paginas;
import jakarta.persistence.criteria.Predicate;

/**
 * Adaptador JPA da porta {@link ServidorPort}.
 */
@Repository
public class ServidorJpaAdapter implements ServidorPort {

    private static final Set<String> COLUNAS_ORDENACAO = Set.of("nome", "ambienteId", "sistemaOperacionalId");

    private final ServidorJpaRepository repositorio;
    private final VinculoPort vinculoPort;

    public ServidorJpaAdapter(ServidorJpaRepository repositorio, VinculoPort vinculoPort) {
        this.repositorio = repositorio;
        this.vinculoPort = vinculoPort;
    }

    @Override
    @Transactional(readOnly = true)
    public Pagina<Servidor> listar(ServidorFiltro filtro) {
        Pageable pageable = Paginas.para(filtro.paginacao(), COLUNAS_ORDENACAO, "nome");
        Page<ServidorEntity> pagina = repositorio.findAll(especificacao(filtro), pageable);
        List<Servidor> itens = pagina.getContent().stream().map(this::paraDominio).toList();
        return Pagina.de(itens, pagina.getNumber(), pagina.getSize(), pagina.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Servidor> buscarPorId(Long id) {
        return repositorio.findById(id).map(this::paraDominio);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Servidor> buscarPorNome(String nome) {
        return repositorio.findFirstByNomeIgnoreCase(nome).map(this::paraDominio);
    }

    @Override
    @Transactional
    public Servidor inserir(Servidor servidor) {
        ServidorEntity entity = new ServidorEntity();
        aplicar(entity, servidor);
        return paraDominio(repositorio.save(entity));
    }

    @Override
    @Transactional
    public Servidor alterar(Servidor servidor) {
        ServidorEntity entity = repositorio.findById(servidor.id()).orElseThrow();
        aplicar(entity, servidor);
        return paraDominio(repositorio.save(entity));
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        vinculoPort.removerTodos(id, TipoVinculo.BACKUP);
        vinculoPort.removerTodos(id, TipoVinculo.BASE_DE_DADO);
        vinculoPort.desvincularDonos(id, TipoVinculo.SERVIDOR);
        repositorio.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Servidor> listarPorIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return repositorio.findAllById(ids).stream().map(this::paraDominio).toList();
    }

    private Specification<ServidorEntity> especificacao(ServidorFiltro filtro) {
        return (raiz, consulta, criteriaBuilder) -> {
            List<Predicate> condicoes = new ArrayList<>();

            if (filtro.termo() != null) {
                String busca = "%" + filtro.termo().toUpperCase() + "%";
                condicoes.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.upper(raiz.get("nome")), busca),
                        criteriaBuilder.like(criteriaBuilder.upper(raiz.get("aplicacao")), busca)));
            }
            if (filtro.ambienteId() != null) {
                condicoes.add(criteriaBuilder.equal(raiz.get("ambienteId"), filtro.ambienteId()));
            }
            if (filtro.sistemaOperacionalId() != null) {
                condicoes.add(criteriaBuilder.equal(raiz.get("sistemaOperacionalId"), filtro.sistemaOperacionalId()));
            }
            if (filtro.ligado() != null) {
                condicoes.add(criteriaBuilder.equal(raiz.get("ligado"), Indicadores.de(filtro.ligado())));
            }
            return condicoes.isEmpty() ? criteriaBuilder.conjunction()
                    : criteriaBuilder.and(condicoes.toArray(Predicate[]::new));
        };
    }

    private void aplicar(ServidorEntity entity, Servidor servidor) {
        entity.setNome(servidor.nome());
        entity.setLigado(Indicadores.de(servidor.ligado()));
        entity.setCpu(servidor.cpu());
        entity.setMemoriaGb(servidor.memoriaGb());
        entity.setSistemaOperacionalId(servidor.sistemaOperacionalId());
        entity.setTamanhoGb(servidor.tamanhoGb());
        entity.setArmazenamentoId(servidor.armazenamentoId());
        entity.setAmbienteId(servidor.ambienteId());
        entity.setAplicacao(servidor.aplicacao());
        entity.setBackup(Indicadores.de(servidor.possuiBackup()));
    }

    private Servidor paraDominio(ServidorEntity entity) {
        return Servidor.reconstituir(entity.getId(),
                new Servidor.Dados(entity.getNome(),
                        Indicadores.para(entity.getLigado()),
                        entity.getCpu(),
                        entity.getMemoriaGb(),
                        entity.getSistemaOperacionalId(),
                        entity.getTamanhoGb(),
                        entity.getArmazenamentoId(),
                        entity.getAmbienteId(),
                        entity.getAplicacao()),
                Indicadores.para(entity.getBackup()));
    }
}