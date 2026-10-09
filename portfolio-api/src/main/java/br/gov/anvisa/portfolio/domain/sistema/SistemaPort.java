package br.gov.anvisa.portfolio.domain.sistema;

import java.util.Optional;

import br.gov.anvisa.portfolio.domain.shared.Pagina;

/**
 * Porta de saida para o agregado Sistema.
 */
public interface SistemaPort {

    Pagina<Sistema> listar(SistemaFiltro filtro);

    Optional<Sistema> buscarPorId(Long id);

    Optional<Sistema> buscarPorSigla(String sigla);

    Sistema inserir(Sistema sistema);

    Sistema alterar(Sistema sistema);

    void excluir(Long id);

    /**
     * @param ids identificadores dos sistemas
     * @return sistemas ativos (registro ativo = S) encontrados
     */
    java.util.List<Sistema> listarAtivosPorIds(java.util.Collection<Long> ids);
}