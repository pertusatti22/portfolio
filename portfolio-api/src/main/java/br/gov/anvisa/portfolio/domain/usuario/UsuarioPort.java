package br.gov.anvisa.portfolio.domain.usuario;

import java.util.Optional;

import br.gov.anvisa.portfolio.domain.shared.Pagina;

/**
 * Porta de saida para o agregado Usuario e para o historico de vigencias.
 */
public interface UsuarioPort {

    Pagina<Usuario> listar(UsuarioFiltro filtro);

    Optional<Usuario> buscarPorUsername(String username);

    Usuario inserir(Usuario usuario);

    Usuario alterar(Usuario usuario);

    void excluir(String username);

    Pagina<UsuarioHistorico> listarHistorico(UsuarioHistoricoFiltro filtro);
}