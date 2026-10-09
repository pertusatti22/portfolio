package br.gov.anvisa.portfolio.domain.catalogo;

import java.util.List;
import java.util.Optional;

import br.gov.anvisa.portfolio.domain.shared.Pagina;
import br.gov.anvisa.portfolio.domain.shared.PaginaRequest;

/**
 * Porta de saida (interface de dominio) para persistencia de catalogos.
 */
public interface CatalogoPort {

    Pagina<Catalogo> listar(CatalogoTipo tipo, String filtro, boolean somenteAtivos, PaginaRequest paginacao);

    /**
     * Lista o catalogo sem paginacao, utilizado para preencher listas suspensas.
     */
    List<Catalogo> listarTodos(CatalogoTipo tipo, boolean somenteAtivos);

    Optional<Catalogo> buscarPorId(CatalogoTipo tipo, Long id);

    Optional<Catalogo> buscarPorDescricao(CatalogoTipo tipo, String descricao);

    Catalogo inserir(CatalogoTipo tipo, Catalogo catalogo);

    Catalogo alterar(CatalogoTipo tipo, Catalogo catalogo);

    void desativar(CatalogoTipo tipo, Long id);

    void excluir(CatalogoTipo tipo, Long id);
}