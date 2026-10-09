package br.gov.anvisa.portfolio.domain.url;

import java.util.Optional;

import br.gov.anvisa.portfolio.domain.shared.Pagina;
import br.gov.anvisa.portfolio.domain.shared.PaginaRequest;
import br.gov.anvisa.portfolio.domain.shared.Textos;

/**
 * Porta de saida para o agregado URL.
 */
public interface UrlPort {

    Pagina<Url> listar(UrlFiltro filtro);

    Optional<Url> buscarPorId(Long id);

    Url inserir(Url url);

    Url alterar(Url url);

    void excluir(Long id);
}