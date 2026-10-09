package br.gov.anvisa.portfolio.domain.usuario;

import br.gov.anvisa.portfolio.domain.shared.PaginaRequest;
import br.gov.anvisa.portfolio.domain.shared.Textos;

/**
 * Filtro de consulta do agregado Usuario.
 *
 * @param termo               pesquisa livre (usuario ou host)
 * @param tipoUsuarioId       codigo do tipo de usuario
 * @param usuarioBancoDadosId codigo do usuario de banco de dados
 * @param somenteVigentes     quando verdadeiro, lista apenas usuarios com vigencia em aberto
 * @param paginacao           paginacao e ordenacao
 */
public record UsuarioFiltro(String termo,
                            Long tipoUsuarioId,
                            Long usuarioBancoDadosId,
                            Boolean somenteVigentes,
                            PaginaRequest paginacao) {

    public UsuarioFiltro {
        String normalizado = Textos.normalizar(termo);
        termo = normalizado == null || normalizado.isEmpty() ? null : normalizado;
        paginacao = paginacao == null ? PaginaRequest.padrao("username") : paginacao;
    }

    public static UsuarioFiltro padrao(PaginaRequest paginacao) {
        return new UsuarioFiltro(null, null, null, Boolean.TRUE, paginacao);
    }
}