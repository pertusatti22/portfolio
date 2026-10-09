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

import br.gov.anvisa.portfolio.domain.rotina.Rotina;
import br.gov.anvisa.portfolio.domain.rotina.RotinaFiltro;
import br.gov.anvisa.portfolio.domain.rotina.RotinaPort;
import br.gov.anvisa.portfolio.domain.shared.Pagina;
import br.gov.anvisa.portfolio.infrastructure.persistence.entity.RotinaEntity;
import br.gov.anvisa.portfolio.infrastructure.persistence.repository.RotinaJpaRepository;
import br.gov.anvisa.portfolio.infrastructure.persistence.support.Indicadores;
import br.gov.anvisa.portfolio.infrastructure.persistence.support.Paginas;
import jakarta.persistence.criteria.Predicate;

/**
 * Adaptador JPA da porta {@link RotinaPort}.
 */
@Repository
public class RotinaJpaAdapter implements RotinaPort {

    private static final Set<String> COLUNAS_ORDENACAO = Set.of("nomeJob", "owner", "schemaId");

    private final RotinaJpaRepository repositorio;

    public RotinaJpaAdapter(RotinaJpaRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    @Transactional(readOnly = true)
    public Pagina<Rotina> listar(RotinaFiltro filtro) {
        Pageable pageable = Paginas.para(filtro.paginacao(), COLUNAS_ORDENACAO, "nomeJob");
        Page<RotinaEntity> pagina = repositorio.findAll(especificacao(filtro), pageable);
        List<Rotina> itens = pagina.getContent().stream().map(this::paraDominio).toList();
        return Pagina.de(itens, pagina.getNumber(), pagina.getSize(), pagina.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rotina> buscarPorId(Long id) {
        return repositorio.findById(id).map(this::paraDominio);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rotina> buscarPorOwnerENomeJob(String owner, String nomeJob) {
        return repositorio.findByOwnerAndNomeJobIgnoreCase(owner, nomeJob).map(this::paraDominio);
    }

    @Override
    @Transactional
    public Rotina inserir(Rotina rotina) {
        RotinaEntity entity = new RotinaEntity();
        aplicar(entity, rotina);
        return paraDominio(repositorio.save(entity));
    }

    @Override
    @Transactional
    public Rotina alterar(Rotina rotina) {
        RotinaEntity entity = repositorio.findById(rotina.id()).orElseThrow();
        aplicar(entity, rotina);
        return paraDominio(repositorio.save(entity));
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        repositorio.deleteById(id);
    }

    private Specification<RotinaEntity> especificacao(RotinaFiltro filtro) {
        return (raiz, consulta, criteriaBuilder) -> {
            List<Predicate> condicoes = new ArrayList<>();

            if (filtro.termo() != null) {
                condicoes.add(criteriaBuilder.like(criteriaBuilder.upper(raiz.get("nomeJob")),
                        "%" + filtro.termo().toUpperCase() + "%"));
            }
            if (filtro.schemaId() != null) {
                condicoes.add(criteriaBuilder.equal(raiz.get("schemaId"), filtro.schemaId()));
            }
            if (filtro.owner() != null) {
                condicoes.add(criteriaBuilder.like(criteriaBuilder.upper(raiz.get("owner")),
                        "%" + filtro.owner().toUpperCase() + "%"));
            }
            if (filtro.ativo() != null) {
                condicoes.add(criteriaBuilder.equal(raiz.get("registroAtivo"), Indicadores.de(filtro.ativo())));
            }
            return condicoes.isEmpty() ? criteriaBuilder.conjunction()
                    : criteriaBuilder.and(condicoes.toArray(Predicate[]::new));
        };
    }

    private void aplicar(RotinaEntity entity, Rotina rotina) {
        entity.setSchemaId(rotina.schemaId());
        entity.setOwner(rotina.owner());
        entity.setNomeJob(rotina.nomeJob());
        entity.setIntervaloExecucao(rotina.intervaloExecucao());
        entity.setRegistroAtivo(Indicadores.de(rotina.registroAtivo()));
    }

    private Rotina paraDominio(RotinaEntity entity) {
        return Rotina.reconstituir(entity.getId(),
                entity.getSchemaId(),
                entity.getOwner(),
                entity.getNomeJob(),
                entity.getIntervaloExecucao(),
                Indicadores.para(entity.getRegistroAtivo()));
    }
}