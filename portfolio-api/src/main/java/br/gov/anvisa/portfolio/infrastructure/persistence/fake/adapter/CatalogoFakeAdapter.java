package br.gov.anvisa.portfolio.infrastructure.persistence.fake.adapter;

import br.gov.anvisa.portfolio.domain.catalogo.Catalogo;
import br.gov.anvisa.portfolio.domain.catalogo.CatalogoTipo;
import br.gov.anvisa.portfolio.domain.catalogo.CatalogoPort;
import br.gov.anvisa.portfolio.domain.shared.Pagina;
import br.gov.anvisa.portfolio.domain.shared.PaginaRequest;
import br.gov.anvisa.portfolio.infrastructure.persistence.fake.FakeDataStore;
import br.gov.anvisa.portfolio.infrastructure.persistence.fake.dto.FakeCatalogoDto;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Profile("dev")
@Primary
public class CatalogoFakeAdapter implements CatalogoPort {
    private final FakeDataStore store;

    public CatalogoFakeAdapter(FakeDataStore store) {
        this.store = store;
    }

    @Override
    public Pagina<Catalogo> listar(CatalogoTipo tipo, String filtro, boolean somenteAtivos, PaginaRequest paginacao) {
        List<Catalogo> todos = store.catalogos.stream()
                .filter(c -> tipo == null || tipo.name().equals(c.tipo()))
                .filter(c -> filtro == null || ((c.descricao() != null && c.descricao().toUpperCase().contains(filtro.toUpperCase())) || (c.codigo() != null && c.codigo().toUpperCase().contains(filtro.toUpperCase()))))
                .filter(c -> !somenteAtivos || Boolean.TRUE.equals(c.ativo()))
                .sorted(Comparator.comparing(FakeCatalogoDto::ordem, Comparator.nullsLast(Integer::compareTo)))
                .map(this::toDomain)
                .collect(Collectors.toList());
        int page = paginacao != null ? paginacao.pagina() : 0;
        int size = paginacao != null ? paginacao.tamanho() : PaginaRequest.TAMANHO_PADRAO;
        int start = page * size;
        int end = Math.min(start + size, todos.size());
        List<Catalogo> itens = start > end ? List.of() : todos.subList(start, end);
        return Pagina.de(itens, page, size, todos.size());
    }

    @Override
    public List<Catalogo> listarTodos(CatalogoTipo tipo, boolean somenteAtivos) {
        PaginaRequest pr = new PaginaRequest(0, Integer.MAX_VALUE, null, true);
        return listar(tipo, null, somenteAtivos, pr).itens();
    }

    @Override
    public Optional<Catalogo> buscarPorId(CatalogoTipo tipo, Long id) {
        String cod = id == null ? null : String.valueOf(id);
        return store.catalogos.stream()
                .filter(c -> tipo != null && tipo.name().equals(c.tipo()))
                .filter(c -> cod != null && cod.equals(c.codigo()))
                .findFirst()
                .map(this::toDomain);
    }

    @Override
    public Optional<Catalogo> buscarPorDescricao(CatalogoTipo tipo, String descricao) {
        return store.catalogos.stream()
                .filter(c -> tipo != null && tipo.name().equals(c.tipo()))
                .filter(c -> descricao != null && descricao.equalsIgnoreCase(c.descricao()))
                .findFirst()
                .map(this::toDomain);
    }

    @Override
    public Catalogo inserir(CatalogoTipo tipo, Catalogo catalogo) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Catalogo alterar(CatalogoTipo tipo, Catalogo catalogo) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void desativar(CatalogoTipo tipo, Long id) {
    }

    @Override
    public void excluir(CatalogoTipo tipo, Long id) {
    }

    private Catalogo toDomain(FakeCatalogoDto c) {
        Long id = null;
        try {
            id = Long.parseLong(c.codigo() == null ? "0" : c.codigo());
        } catch (Exception ignored) {
        }
        return new Catalogo(id, c.descricao(), null, c.liberado(), Boolean.TRUE.equals(c.ativo()));
    }
}
