package br.gov.anvisa.portfolio.domain.documentacao;

import java.util.Optional;

import br.gov.anvisa.portfolio.domain.shared.Pagina;
import br.gov.anvisa.portfolio.domain.shared.PaginaRequest;

/**
 * Porta de saida para o agregado Documentacao (vinculada a um sistema).
 */
public interface DocumentacaoPort {

    Pagina<Documentacao> listarPorSistema(Long sistemaId, Long tipoDocumentacaoId, Boolean ativo, PaginaRequest paginacao);

    Optional<Documentacao> buscarPorId(Long id);

    Documentacao inserir(Documentacao documentacao);

    Documentacao alterar(Documentacao documentacao);

    void excluir(Long id);

    long contarPorSistema(Long sistemaId);
}