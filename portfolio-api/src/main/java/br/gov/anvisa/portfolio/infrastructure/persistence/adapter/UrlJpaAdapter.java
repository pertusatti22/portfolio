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

import br.gov.anvisa.portfolio.domain.shared.Pagina;
import br.gov.anvisa.portfolio.domain.url.TipoUrl;
import br.gov.anvisa.portfolio.domain.url.Url;
import br.gov.anvisa.portfolio.domain.url.UrlFiltro;
import br.gov.anvisa.portfolio.domain.url.UrlPort;
import br.gov.anvisa.portfolio.domain.vinculo.TipoVinculo;
import br.gov.anvisa.portfolio.domain.vinculo.VinculoPort;
import br.gov.anvisa.portfolio.infrastructure.persistence.entity.UrlEntity;
import br.gov.anvisa.portfolio.infrastructure.persistence.repository.UrlJpaRepository;
import br.gov.anvisa.portfolio.infrastructure.persistence.support.Indicadores;
import br.gov.anvisa.portfolio.infrastructure.persistence.support.Paginas;
import jakarta.persistence.criteria.Predicate;

/**
 * Adaptador JPA da porta {@link UrlPort}.
 *
 * <p>O filtro por sistema utiliza a tabela de relacionamento RL_SISTEMA_URL, consultada
 * atraves de {@link VinculoPort}.
 */
@Repository
public class UrlJpaAdapter implements UrlPort {

    private static final Set<String> COLUNAS_ORDENACAO = Set.of("endereco", "tipo", "ambienteId", "acessoId");

    private final UrlJpaRepository repositorio;
    private final VinculoPort vinculoPort;

    public UrlJpaAdapter(UrlJpaRepository repositorio, VinculoPort vinculoPort) {
        this.repositorio = repositorio;
        this.vinculoPort = vinculoPort;
    }

    @Override
    @Transactional(readOnly = true)
    public Pagina<Url> listar(UrlFiltro filtro) {
        Pageable pageable = Paginas.para(filtro.paginacao(), COLUNAS_ORDENACAO, "endereco");
        Page<UrlEntity> pagina = repositorio.findAll(especificacao(filtro), pageable);
        List<Url> itens = pagina.getContent().stream().map(this::paraDominio).toList();
        return Pagina.de(itens, pagina.getNumber(), pagina.getSize(), pagina.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Url> buscarPorId(Long id) {
        return repositorio.findById(id).map(this::paraDominio);
    }

    @Override
    @Transactional
    public Url inserir(Url url) {
        UrlEntity entity = new UrlEntity();
        aplicar(entity, url);
        return paraDominio(repositorio.save(entity));
    }

    @Override
    @Transactional
    public Url alterar(Url url) {
        UrlEntity entity = repositorio.findById(url.id()).orElseThrow();
        aplicar(entity, url);
        return paraDominio(repositorio.save(entity));
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        vinculoPort.desvincularDonos(id, TipoVinculo.URL);
        repositorio.deleteById(id);
    }

    private Specification<UrlEntity> especificacao(UrlFiltro filtro) {
        return (raiz, consulta, criteriaBuilder) -> {
            List<Predicate> condicoes = new ArrayList<>();

            if (filtro.termo() != null) {
                condicoes.add(criteriaBuilder.like(criteriaBuilder.upper(raiz.get("endereco")),
                        "%" + filtro.termo().toUpperCase() + "%"));
            }
            if (filtro.tipo() != null) {
                condicoes.add(criteriaBuilder.equal(raiz.get("tipo"), String.valueOf(filtro.tipo().codigo())));
            }
            if (filtro.ambienteId() != null) {
                condicoes.add(criteriaBuilder.equal(raiz.get("ambienteId"), filtro.ambienteId()));
            }
            if (filtro.ativo() != null) {
                condicoes.add(criteriaBuilder.equal(raiz.get("registroAtivo"), Indicadores.de(filtro.ativo())));
            }
            if (filtro.sistemaId() != null) {
                List<Long> urlIds = vinculoPort.listarVinculados(filtro.sistemaId(), TipoVinculo.URL);
                if (urlIds.isEmpty()) {
                    condicoes.add(criteriaBuilder.disjunction());
                } else {
                    condicoes.add(raiz.get("id").in(urlIds));
                }
            }
            return condicoes.isEmpty() ? criteriaBuilder.conjunction()
                    : criteriaBuilder.and(condicoes.toArray(Predicate[]::new));
        };
    }

    private void aplicar(UrlEntity entity, Url url) {
        entity.setEndereco(url.endereco());
        entity.setTipo(String.valueOf(url.tipo().codigo()));
        entity.setAmbienteId(url.ambienteId());
        entity.setAcessoId(url.acessoId());
        entity.setFrameworkId(url.frameworkId());
        entity.setAutenticacaoId(url.autenticacaoId());
        entity.setRegistroAtivo(Indicadores.de(url.registroAtivo()));
    }

    private Url paraDominio(UrlEntity entity) {
        return Url.reconstituir(entity.getId(),
                TipoUrl.doCodigo(entity.getTipo().charAt(0)),
                new Url.Dados(entity.getEndereco(),
                        entity.getAmbienteId(),
                        entity.getAcessoId(),
                        entity.getFrameworkId(),
                        entity.getAutenticacaoId()),
                Indicadores.para(entity.getRegistroAtivo()));
    }
}