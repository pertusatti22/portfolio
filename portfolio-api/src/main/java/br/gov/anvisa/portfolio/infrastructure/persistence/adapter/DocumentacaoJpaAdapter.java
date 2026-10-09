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

import br.gov.anvisa.portfolio.domain.documentacao.Documentacao;
import br.gov.anvisa.portfolio.domain.documentacao.DocumentacaoPort;
import br.gov.anvisa.portfolio.domain.shared.Pagina;
import br.gov.anvisa.portfolio.domain.shared.PaginaRequest;
import br.gov.anvisa.portfolio.infrastructure.persistence.entity.DocumentacaoEntity;
import br.gov.anvisa.portfolio.infrastructure.persistence.repository.DocumentacaoJpaRepository;
import br.gov.anvisa.portfolio.infrastructure.persistence.support.Indicadores;
import br.gov.anvisa.portfolio.infrastructure.persistence.support.Paginas;
import jakarta.persistence.criteria.Predicate;

/**
 * Adaptador JPA da porta {@link DocumentacaoPort}.
 */
@Repository
public class DocumentacaoJpaAdapter implements DocumentacaoPort {

    private static final Set<String> COLUNAS_ORDENACAO = Set.of("urlRepositorio", "tipoDocumentacaoId");

    private final DocumentacaoJpaRepository repositorio;

    public DocumentacaoJpaAdapter(DocumentacaoJpaRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    @Transactional(readOnly = true)
    public Pagina<Documentacao> listarPorSistema(Long sistemaId,
                                                 Long tipoDocumentacaoId,
                                                 Boolean ativo,
                                                 PaginaRequest paginacao) {
        Pageable pageable = Paginas.para(paginacao, COLUNAS_ORDENACAO, "urlRepositorio");
        Page<DocumentacaoEntity> pagina = repositorio.findAll(especificacao(sistemaId, tipoDocumentacaoId, ativo),
                pageable);
        List<Documentacao> itens = pagina.getContent().stream().map(this::paraDominio).toList();
        return Pagina.de(itens, pagina.getNumber(), pagina.getSize(), pagina.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Documentacao> buscarPorId(Long id) {
        return repositorio.findById(id).map(this::paraDominio);
    }

    @Override
    @Transactional
    public Documentacao inserir(Documentacao documentacao) {
        DocumentacaoEntity entity = new DocumentacaoEntity();
        aplicar(entity, documentacao);
        return paraDominio(repositorio.save(entity));
    }

    @Override
    @Transactional
    public Documentacao alterar(Documentacao documentacao) {
        DocumentacaoEntity entity = repositorio.findById(documentacao.id()).orElseThrow();
        aplicar(entity, documentacao);
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

    private Specification<DocumentacaoEntity> especificacao(Long sistemaId, Long tipoDocumentacaoId, Boolean ativo) {
        return (raiz, consulta, criteriaBuilder) -> {
            List<Predicate> condicoes = new ArrayList<>();
            condicoes.add(criteriaBuilder.equal(raiz.get("sistemaId"), sistemaId));
            if (tipoDocumentacaoId != null) {
                condicoes.add(criteriaBuilder.equal(raiz.get("tipoDocumentacaoId"), tipoDocumentacaoId));
            }
            if (ativo != null) {
                condicoes.add(criteriaBuilder.equal(raiz.get("registroAtivo"), Indicadores.de(ativo)));
            }
            return criteriaBuilder.and(condicoes.toArray(Predicate[]::new));
        };
    }

    private void aplicar(DocumentacaoEntity entity, Documentacao documentacao) {
        entity.setSistemaId(documentacao.sistemaId());
        entity.setUrlRepositorio(documentacao.urlRepositorio());
        entity.setTipoDocumentacaoId(documentacao.tipoDocumentacaoId());
        entity.setRegistroAtivo(Indicadores.de(documentacao.registroAtivo()));
    }

    private Documentacao paraDominio(DocumentacaoEntity entity) {
        return Documentacao.reconstituir(entity.getId(),
                entity.getSistemaId(),
                entity.getUrlRepositorio(),
                entity.getTipoDocumentacaoId(),
                Indicadores.para(entity.getRegistroAtivo()));
    }
}