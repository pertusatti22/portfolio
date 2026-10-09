package br.gov.anvisa.portfolio.domain.rotina;

import br.gov.anvisa.portfolio.domain.shared.PaginaRequest;
import br.gov.anvisa.portfolio.domain.shared.Textos;

/**
 * Filtro de consulta do agregado Rotina.
 *
 * @param termo       pesquisa livre (nome do job)
 * @param schemaId    codigo do schema
 * @param owner       proprietor do job
 * @param ativo       quando preenchido, filtra pelo indicador de registro ativo
 * @param paginacao   paginacao e ordenacao
 */
public record RotinaFiltro(String termo,
                           Long schemaId,
                           String owner,
                           Boolean ativo,
                           PaginaRequest paginacao) {

    public RotinaFiltro {
        String normalizado = Textos.normalizar(termo);
        termo = normalizado == null || normalizado.isEmpty() ? null : normalizado;
        paginacao = paginacao == null ? PaginaRequest.padrao("nomeJob") : paginacao;
    }

    public static RotinaFiltro padrao(PaginaRequest paginacao) {
        return new RotinaFiltro(null, null, null, Boolean.TRUE, paginacao);
    }
}