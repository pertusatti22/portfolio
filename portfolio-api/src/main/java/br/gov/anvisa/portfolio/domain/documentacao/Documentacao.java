package br.gov.anvisa.portfolio.domain.documentacao;

import br.gov.anvisa.portfolio.domain.shared.RegraDeNegocioException;
import br.gov.anvisa.portfolio.domain.shared.Textos;

/**
 * Agregado Documentacao: link de documentacao (manual, guia do usuario etc.) de um sistema.
 */
public final class Documentacao {

    private Long id;
    private Long sistemaId;
    private String urlRepositorio;
    private Long tipoDocumentacaoId;
    private boolean registroAtivo;

    private Documentacao() {
    }

    public static Documentacao criar(Long sistemaId, String urlRepositorio, Long tipoDocumentacaoId) {
        Documentacao documentacao = new Documentacao();
        documentacao.sistemaId = sistemaId;
        documentacao.registroAtivo = true;
        documentacao.atualizarDados(urlRepositorio, tipoDocumentacaoId);
        return documentacao;
    }

    public static Documentacao reconstituir(Long id,
                                            Long sistemaId,
                                            String urlRepositorio,
                                            Long tipoDocumentacaoId,
                                            boolean registroAtivo) {
        Documentacao documentacao = new Documentacao();
        documentacao.id = id;
        documentacao.sistemaId = sistemaId;
        documentacao.registroAtivo = registroAtivo;
        documentacao.atualizarDados(urlRepositorio, tipoDocumentacaoId);
        return documentacao;
    }

    public void atualizarDados(String urlRepositorio, Long tipoDocumentacaoId) {
        this.urlRepositorio = validarUrl(Textos.obrigatorio(urlRepositorio, "URL do repositorio", 600));
        if (tipoDocumentacaoId == null) {
            throw new RegraDeNegocioException("O tipo de documentacao e obrigatorio");
        }
        this.tipoDocumentacaoId = tipoDocumentacaoId;
    }

    public void desativar() {
        this.registroAtivo = false;
    }

    public void ativar() {
        this.registroAtivo = true;
    }

    public Long id() {
        return id;
    }

    public Long sistemaId() {
        return sistemaId;
    }

    public String urlRepositorio() {
        return urlRepositorio;
    }

    public Long tipoDocumentacaoId() {
        return tipoDocumentacaoId;
    }

    public boolean registroAtivo() {
        return registroAtivo;
    }

    private String validarUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw new RegraDeNegocioException("A URL do repositorio deve iniciar com http:// ou https://");
        }
        return url;
    }
}