package br.gov.anvisa.portfolio.domain.servidor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import br.gov.anvisa.portfolio.domain.shared.Pagina;
import br.gov.anvisa.portfolio.domain.shared.Textos;

/**
 * Porta de saida para o agregado Servidor.
 */
public interface ServidorPort {

    Pagina<Servidor> listar(ServidorFiltro filtro);

    Optional<Servidor> buscarPorId(Long id);

    Optional<Servidor> buscarPorNome(String nome);

    Servidor inserir(Servidor servidor);

    Servidor alterar(Servidor servidor);

    void excluir(Long id);

    /**
     * TB_SERVIDOR nao possui indicador de registro ativo; o metodo retorna os hosts encontrados.
     *
     * @param ids identificadores dos servidores
     * @return servidores encontrados
     */
    List<Servidor> listarPorIds(Collection<Long> ids);
}