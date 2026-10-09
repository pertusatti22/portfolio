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

import br.gov.anvisa.portfolio.domain.shared.Pagina;
import br.gov.anvisa.portfolio.domain.sistema.Sistema;
import br.gov.anvisa.portfolio.domain.sistema.SistemaFiltro;
import br.gov.anvisa.portfolio.domain.sistema.SistemaPort;
import br.gov.anvisa.portfolio.domain.vinculo.TipoVinculo;
import br.gov.anvisa.portfolio.domain.vinculo.VinculoPort;
import br.gov.anvisa.portfolio.infrastructure.persistence.entity.SistemaEntity;
import br.gov.anvisa.portfolio.infrastructure.persistence.repository.SistemaJpaRepository;
import br.gov.anvisa.portfolio.infrastructure.persistence.support.Indicadores;
import br.gov.anvisa.portfolio.infrastructure.persistence.support.Paginas;
import jakarta.persistence.criteria.Predicate;

/**
 * Adaptador JPA da porta {@link SistemaPort}.
 */
@Repository
public class SistemaJpaAdapter implements SistemaPort {

    private static final Set<String> COLUNAS_ORDENACAO = Set.of("sigla", "nome", "objetivo", "origemId", "baseDeDadoId");

    private final SistemaJpaRepository repositorio;
    private final VinculoPort vinculoPort;

    public SistemaJpaAdapter(SistemaJpaRepository repositorio, VinculoPort vinculoPort) {
        this.repositorio = repositorio;
        this.vinculoPort = vinculoPort;
    }

    @Override
    @Transactional(readOnly = true)
    public Pagina<Sistema> listar(SistemaFiltro filtro) {
        Pageable pageable = Paginas.para(filtro.paginacao(), COLUNAS_ORDENACAO, "nome");
        Page<SistemaEntity> pagina = repositorio.findAll(especificacao(filtro), pageable);
        List<Sistema> itens = pagina.getContent().stream().map(this::paraDominio).toList();
        return Pagina.de(itens, pagina.getNumber(), pagina.getSize(), pagina.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Sistema> buscarPorId(Long id) {
        return repositorio.findById(id).map(this::paraDominio);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Sistema> buscarPorSigla(String sigla) {
        return repositorio.findAll((root, query, cb) -> {
            if (sigla == null) {
                return null;
            }
            return cb.equal(cb.upper(root.get("sigla")), sigla.toUpperCase());
        }).stream().findFirst().map(this::paraDominio);
    }

    @Override
    @Transactional
    public Sistema inserir(Sistema sistema) {
        SistemaEntity entity = new SistemaEntity();
        aplicar(entity, sistema);
        return paraDominio(repositorio.save(entity));
    }

    @Override
    @Transactional
    public Sistema alterar(Sistema sistema) {
        SistemaEntity entity = repositorio.findById(sistema.id()).orElseThrow();
        aplicar(entity, sistema);
        return paraDominio(repositorio.save(entity));
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        vinculoPort.removerTodos(id, TipoVinculo.SCHEMA);
        vinculoPort.removerTodos(id, TipoVinculo.SOFTWARE);
        vinculoPort.removerTodos(id, TipoVinculo.URL);
        vinculoPort.removerTodos(id, TipoVinculo.SERVIDOR);
        repositorio.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sistema> listarAtivosPorIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return repositorio.findAllById(ids).stream()
                .filter(entity -> Indicadores.para(entity.getRegistroAtivo()))
                .map(this::paraDominio)
                .toList();
    }

    private Specification<SistemaEntity> especificacao(SistemaFiltro filtro) {
        return (raiz, consulta, criteriaBuilder) -> {
            List<Predicate> condicoes = new ArrayList<>();

            if (filtro.termo() != null) {
                String busca = "%" + filtro.termo().toUpperCase() + "%";
                condicoes.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.upper(raiz.get("sigla")), busca),
                        criteriaBuilder.like(criteriaBuilder.upper(raiz.get("nome")), busca),
                        criteriaBuilder.like(criteriaBuilder.upper(raiz.get("objetivo")), busca)));
            }
            if (filtro.origemId() != null) {
                condicoes.add(criteriaBuilder.equal(raiz.get("origemId"), filtro.origemId()));
            }
            if (filtro.baseDeDadoId() != null) {
                condicoes.add(criteriaBuilder.equal(raiz.get("baseDeDadoId"), filtro.baseDeDadoId()));
            }
            if (filtro.ativo() != null) {
                condicoes.add(criteriaBuilder.equal(raiz.get("registroAtivo"), Indicadores.de(filtro.ativo())));
            }
            if (filtro.validado() != null) {
                condicoes.add(criteriaBuilder.equal(raiz.get("validado"), Indicadores.de(filtro.validado())));
            }
            return condicoes.isEmpty() ? criteriaBuilder.conjunction()
                    : criteriaBuilder.and(condicoes.toArray(Predicate[]::new));
        };
    }

    private void aplicar(SistemaEntity entity, Sistema sistema) {
        entity.setSigla(sistema.sigla());
        entity.setNome(sistema.nome());
        entity.setOrigemId(sistema.origemId());
        entity.setObjetivo(sistema.objetivo());
        entity.setArquivo(sistema.arquivo());
        entity.setAnvisa(Indicadores.de(sistema.anvisa()));
        entity.setIntegracaoContinua(Indicadores.de(sistema.integracaoContinua()));
        entity.setClassificacaoBem(sistema.classificacaoBem());
        entity.setBaseDeDadoId(sistema.baseDeDadoId());
        entity.setRegistroAtivo(Indicadores.de(sistema.registroAtivo()));
        entity.setValidado(Indicadores.de(sistema.validado()));
    }

    private Sistema paraDominio(SistemaEntity entity) {
        return Sistema.reconstituir(entity.getId(),
                new Sistema.Dados(entity.getSigla(),
                        entity.getNome(),
                        entity.getOrigemId(),
                        entity.getObjetivo(),
                        entity.getArquivo(),
                        Indicadores.para(entity.getAnvisa()),
                        Indicadores.para(entity.getIntegracaoContinua()),
                        entity.getClassificacaoBem(),
                        entity.getBaseDeDadoId()),
                Indicadores.para(entity.getRegistroAtivo()),
                Indicadores.para(entity.getValidado()));
    }
}