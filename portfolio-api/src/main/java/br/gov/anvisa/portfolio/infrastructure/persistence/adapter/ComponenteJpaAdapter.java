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

import br.gov.anvisa.portfolio.domain.componente.Componente;
import br.gov.anvisa.portfolio.domain.componente.ComponentePort;
import br.gov.anvisa.portfolio.domain.shared.Pagina;
import br.gov.anvisa.portfolio.domain.shared.PaginaRequest;
import br.gov.anvisa.portfolio.infrastructure.persistence.entity.ComponenteEntity;
import br.gov.anvisa.portfolio.infrastructure.persistence.repository.ComponenteJpaRepository;
import br.gov.anvisa.portfolio.infrastructure.persistence.support.Indicadores;
import br.gov.anvisa.portfolio.infrastructure.persistence.support.Paginas;
import jakarta.persistence.criteria.Predicate;

/**
 * Adaptador JPA da porta {@link ComponentePort}.
 */
@Repository
public class ComponenteJpaAdapter implements ComponentePort {

    private static final Set<String> COLUNAS_ORDENACAO = Set.of("nome", "descricao");

    private final ComponenteJpaRepository repositorio;

    public ComponenteJpaAdapter(ComponenteJpaRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    @Transactional(readOnly = true)
    public Pagina<Componente> listarPorSistema(Long sistemaId, Boolean ativo, PaginaRequest paginacao) {
        Pageable pageable = Paginas.para(paginacao, COLUNAS_ORDENACAO, "nome");
        Page<ComponenteEntity> pagina = repositorio.findAll(especificacao(sistemaId, ativo), pageable);
        List<Componente> itens = pagina.getContent().stream().map(this::paraDominio).toList();
        return Pagina.de(itens, pagina.getNumber(), pagina.getSize(), pagina.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Componente> buscarPorId(Long id) {
        return repositorio.findById(id).map(this::paraDominio);
    }

    @Override
    @Transactional
    public Componente inserir(Componente componente) {
        ComponenteEntity entity = new ComponenteEntity();
        aplicar(entity, componente);
        return paraDominio(repositorio.save(entity));
    }

    @Override
    @Transactional
    public Componente alterar(Componente componente) {
        ComponenteEntity entity = repositorio.findById(componente.id()).orElseThrow();
        aplicar(entity, componente);
        return paraDominio(repositorio.save(entity));
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        repositorio.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long contarPorSistema(Long sistemaId) {
        return repositorio.countBySistemaIdAndRegistroAtivo(sistemaId, Indicadores.SIM);
    }

    private Specification<ComponenteEntity> especificacao(Long sistemaId, Boolean ativo) {
        return (raiz, consulta, criteriaBuilder) -> {
            List<Predicate> condicoes = new ArrayList<>();
            condicoes.add(criteriaBuilder.equal(raiz.get("sistemaId"), sistemaId));
            if (ativo != null) {
                condicoes.add(criteriaBuilder.equal(raiz.get("registroAtivo"), Indicadores.de(ativo)));
            }
            return criteriaBuilder.and(condicoes.toArray(Predicate[]::new));
        };
    }

    private void aplicar(ComponenteEntity entity, Componente componente) {
        entity.setSistemaId(componente.sistemaId());
        entity.setNome(componente.nome());
        entity.setDescricao(componente.descricao());
        entity.setRegistroAtivo(Indicadores.de(componente.registroAtivo()));
    }

    private Componente paraDominio(ComponenteEntity entity) {
        return Componente.reconstituir(entity.getId(),
                entity.getSistemaId(),
                entity.getNome(),
                entity.getDescricao(),
                Indicadores.para(entity.getRegistroAtivo()));
    }
}