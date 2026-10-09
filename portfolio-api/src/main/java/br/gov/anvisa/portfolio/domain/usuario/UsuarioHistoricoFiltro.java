package br.gov.anvisa.portfolio.domain.usuario;

import br.gov.anvisa.portfolio.domain.shared.PaginaRequest;
import br.gov.anvisa.portfolio.domain.shared.Textos;

/**
 * Filtro de consulta do historico de usuarios (TB_HIST_USUARIO).
 *
 * @param username       nome de login
 * @param tipoUsuarioId  codigo do tipo de usuario
 * @param operacao       tipo de operacao registrada
 * @param paginacao      paginacao e ordenacao
 */
public record UsuarioHistoricoFiltro(String username,
                                     Long tipoUsuarioId,
                                     TipoOperacao operacao,
                                     PaginaRequest paginacao) {

    public UsuarioHistoricoFiltro {
        String normalizado = Textos.normalizar(username);
        username = normalizado == null || normalizado.isEmpty() ? null : normalizado;
        paginacao = paginacao == null ? PaginaRequest.padrao("dataRegistro") : paginacao;
    }
}