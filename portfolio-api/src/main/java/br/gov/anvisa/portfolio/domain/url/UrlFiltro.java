package br.gov.anvisa.portfolio.domain.url;

import br.gov.anvisa.portfolio.domain.shared.PaginaRequest;
import br.gov.anvisa.portfolio.domain.shared.Textos;

/**
 * Filtro de consulta do agregado URL.
 *
 * @param termo       pesquisa livre (endereco)
 * @param sistemaId   codigo do sistema vinculado
 * @param tipo        tipo de URL
 * @param ambienteId  codigo do ambiente
 * @param ativo       quando preenchido, filtra pelo indicador de registro ativo
 * @param paginacao   paginacao e ordenacao
 */
public record UrlFiltro(String termo,
                        Long sistemaId,
                        TipoUrl tipo,
                        Long ambienteId,
                        Boolean ativo,
                        PaginaRequest paginacao) {

    public UrlFiltro {
        String normalizado = Textos.normalizar(termo);
        termo = normalizado == null || normalizado.isEmpty() ? null : normalizado;
        paginacao = paginacao == null ? PaginaRequest.padrao("endereco") : paginacao;
    }

    public static UrlFiltro padrao(PaginaRequest paginacao) {
        return new UrlFiltro(null, null, null, null, Boolean.TRUE, paginacao);
    }
}