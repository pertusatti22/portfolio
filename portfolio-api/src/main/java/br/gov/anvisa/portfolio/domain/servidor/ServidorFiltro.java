package br.gov.anvisa.portfolio.domain.servidor;

import br.gov.anvisa.portfolio.domain.shared.PaginaRequest;
import br.gov.anvisa.portfolio.domain.shared.Textos;

/**
 * Filtro de consulta do agregado Servidor.
 *
 * @param termo                 pesquisa livre (nome ou aplicacao)
 * @param ambienteId            codigo do ambiente
 * @param sistemaOperacionalId  codigo do sistema operacional
 * @param ligado                quando preenchido, filtra servidores ligados/desligados
 * @param paginacao             paginacao e ordenacao
 */
public record ServidorFiltro(String termo,
                             Long ambienteId,
                             Long sistemaOperacionalId,
                             Boolean ligado,
                             PaginaRequest paginacao) {

    public ServidorFiltro {
        String normalizado = Textos.normalizar(termo);
        termo = normalizado == null || normalizado.isEmpty() ? null : normalizado;
        paginacao = paginacao == null ? PaginaRequest.padrao("nome") : paginacao;
    }

    public static ServidorFiltro padrao(PaginaRequest paginacao) {
        return new ServidorFiltro(null, null, null, null, paginacao);
    }
}