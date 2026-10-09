package br.gov.anvisa.portfolio.domain.rotina;

import java.util.Optional;

import br.gov.anvisa.portfolio.domain.shared.Pagina;

/**
 * Porta de saida para o agregado Rotina.
 */
public interface RotinaPort {

    Pagina<Rotina> listar(RotinaFiltro filtro);

    Optional<Rotina> buscarPorId(Long id);

    Optional<Rotina> buscarPorOwnerENomeJob(String owner, String nomeJob);

    Rotina inserir(Rotina rotina);

    Rotina alterar(Rotina rotina);

    void excluir(Long id);
}