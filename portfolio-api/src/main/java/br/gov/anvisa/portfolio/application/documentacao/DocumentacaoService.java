package br.gov.anvisa.portfolio.application.documentacao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.anvisa.portfolio.application.catalogo.CatalogoService;
import br.gov.anvisa.portfolio.application.documentacao.dto.DocumentacaoRequest;
import br.gov.anvisa.portfolio.application.documentacao.dto.DocumentacaoResponse;
import br.gov.anvisa.portfolio.domain.catalogo.CatalogoTipo;
import br.gov.anvisa.portfolio.domain.documentacao.Documentacao;
import br.gov.anvisa.portfolio.domain.documentacao.DocumentacaoPort;
import br.gov.anvisa.portfolio.domain.shared.Pagina;
import br.gov.anvisa.portfolio.domain.shared.PaginaRequest;
import br.gov.anvisa.portfolio.domain.shared.RecursoNaoEncontradoException;

/**
 * Casos de uso do agregado Documentacao (sub-recurso de Sistema).
 */
@Service
public class DocumentacaoService {

    private final DocumentacaoPort documentacaoPort;
    private final CatalogoService catalogoService;

    public DocumentacaoService(DocumentacaoPort documentacaoPort, CatalogoService catalogoService) {
        this.documentacaoPort = documentacaoPort;
        this.catalogoService = catalogoService;
    }

    @Transactional(readOnly = true)
    public Pagina<DocumentacaoResponse> listarPorSistema(Long sistemaId,
                                                          Long tipoDocumentacaoId,
                                                          Boolean ativo,
                                                          PaginaRequest paginacao) {
        return documentacaoPort.listarPorSistema(sistemaId, tipoDocumentacaoId, ativo, paginacao)
                .map(this::paraResponse);
    }

    @Transactional(readOnly = true)
    public DocumentacaoResponse buscar(Long sistemaId, Long documentacaoId) {
        return paraResponse(obterDoSistema(sistemaId, documentacaoId));
    }

    @Transactional
    public DocumentacaoResponse criar(Long sistemaId, DocumentacaoRequest request) {
        Documentacao documentacao = Documentacao.criar(sistemaId, request.urlRepositorio(), request.tipoDocumentacaoId());
        if (!request.registroAtivo()) {
            documentacao.desativar();
        }
        return paraResponse(documentacaoPort.inserir(documentacao));
    }

    @Transactional
    public DocumentacaoResponse alterar(Long sistemaId, Long documentacaoId, DocumentacaoRequest request) {
        Documentacao documentacao = obterDoSistema(sistemaId, documentacaoId);
        documentacao.atualizarDados(request.urlRepositorio(), request.tipoDocumentacaoId());
        if (request.registroAtivo()) {
            documentacao.ativar();
        } else {
            documentacao.desativar();
        }
        return paraResponse(documentacaoPort.alterar(documentacao));
    }

    @Transactional
    public void excluir(Long sistemaId, Long documentacaoId) {
        obterDoSistema(sistemaId, documentacaoId);
        documentacaoPort.excluir(documentacaoId);
    }

    private Documentacao obterDoSistema(Long sistemaId, Long documentacaoId) {
        return documentacaoPort.buscarPorId(documentacaoId)
                .filter(documentacao -> documentacao.sistemaId().equals(sistemaId))
                .orElseThrow(() -> new RecursoNaoEncontradoException("Documentacao", documentacaoId));
    }

    private DocumentacaoResponse paraResponse(Documentacao documentacao) {
        return new DocumentacaoResponse(documentacao.id(),
                documentacao.sistemaId(),
                documentacao.urlRepositorio(),
                documentacao.tipoDocumentacaoId(),
                catalogoService.descrever(CatalogoTipo.TIPO_DOCUMENTACAO, documentacao.tipoDocumentacaoId()),
                documentacao.registroAtivo());
    }
}