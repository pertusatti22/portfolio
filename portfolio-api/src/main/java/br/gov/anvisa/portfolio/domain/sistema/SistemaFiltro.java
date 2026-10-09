package br.gov.anvisa.portfolio.domain.sistema;

import br.gov.anvisa.portfolio.domain.shared.PaginaRequest;
import br.gov.anvisa.portfolio.domain.shared.Textos;

/**
 * Filtro de consulta do agregado Sistema.
 *
 * @param termo        pesquisa livre (sigla, nome ou objetivo)
 * @param origemId     codigo da origem
 * @param baseDeDadoId codigo da base de dados
 * @param ativo        quando preenchido, filtra pelo indicador de registro ativo
 * @param validado     quando preenchido, filtra pela validacao dos dados
 * @param paginacao    paginacao e ordenacao
 */
public record SistemaFiltro(String termo,
                            Long origemId,
                            Long baseDeDadoId,
                            Boolean ativo,
                            Boolean validado,
                            PaginaRequest paginacao) {

    public SistemaFiltro {
        String normalizado = Textos.normalizar(termo);
        termo = normalizado == null || normalizado.isEmpty() ? null : normalizado;
        paginacao = paginacao == null ? PaginaRequest.padrao("nome") : paginacao;
    }

    public static SistemaFiltro padrao(PaginaRequest paginacao) {
        return new SistemaFiltro(null, null, null, Boolean.TRUE, null, paginacao);
    }
}