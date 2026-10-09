package br.gov.anvisa.portfolio.domain.componente;

import java.util.Optional;

import br.gov.anvisa.portfolio.domain.shared.Pagina;
import br.gov.anvisa.portfolio.domain.shared.PaginaRequest;

/**
 * Porta de saida para o agregado Componente (vinculado a um sistema).
 */
public interface ComponentePort {

    Pagina<Componente> listarPorSistema(Long sistemaId, Boolean ativo, PaginaRequest paginacao);

    Optional<Componente> buscarPorId(Long id);

    Componente inserir(Componente componente);

    Componente alterar(Componente componente);

    void excluir(Long id);

    long contarPorSistema(Long sistemaId);
}