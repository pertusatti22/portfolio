package br.gov.anvisa.portfolio.application.url;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.anvisa.portfolio.application.catalogo.CatalogoService;
import br.gov.anvisa.portfolio.application.url.dto.UrlRequest;
import br.gov.anvisa.portfolio.application.url.dto.UrlResponse;
import br.gov.anvisa.portfolio.domain.catalogo.CatalogoTipo;
import br.gov.anvisa.portfolio.domain.shared.Pagina;
import br.gov.anvisa.portfolio.domain.shared.RecursoNaoEncontradoException;
import br.gov.anvisa.portfolio.domain.shared.RegraDeNegocioException;
import br.gov.anvisa.portfolio.domain.url.Url;
import br.gov.anvisa.portfolio.domain.url.UrlFiltro;
import br.gov.anvisa.portfolio.domain.url.UrlPort;

/**
 * Casos de uso do agregado URL.
 */
@Service
public class UrlService {

    private final UrlPort urlPort;
    private final CatalogoService catalogoService;

    public UrlService(UrlPort urlPort, CatalogoService catalogoService) {
        this.urlPort = urlPort;
        this.catalogoService = catalogoService;
    }

    @Transactional(readOnly = true)
    public Pagina<UrlResponse> listar(UrlFiltro filtro) {
        return urlPort.listar(filtro).map(this::paraResponse);
    }

    @Transactional(readOnly = true)
    public UrlResponse buscar(Long id) {
        return paraResponse(obterObrigatorio(id));
    }

    @Transactional
    public UrlResponse criar(UrlRequest request) {
        Url url = Url.criar(request.tipo(), extrairDados(request));
        if (!request.registroAtivo()) {
            url.desativar();
        }
        return paraResponse(urlPort.inserir(url));
    }

    @Transactional
    public UrlResponse alterar(Long id, UrlRequest request) {
        Url url = obterObrigatorio(id);
        url.atualizarDados(request.tipo(), extrairDados(request));
        if (request.registroAtivo()) {
            url.ativar();
        } else {
            url.desativar();
        }
        return paraResponse(urlPort.alterar(url));
    }

    @Transactional
    public UrlResponse alterarAtivo(Long id, boolean ativo) {
        Url url = obterObrigatorio(id);
        if (ativo) {
            url.ativar();
        } else {
            url.desativar();
        }
        return paraResponse(urlPort.alterar(url));
    }

    @Transactional
    public void excluir(Long id) {
        obterObrigatorio(id);
        urlPort.excluir(id);
    }

    public Url obterObrigatorio(Long id) {
        return urlPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("URL", id));
    }

    UrlResponse paraResponse(Url url) {
        return new UrlResponse(url.id(),
                url.endereco(),
                url.tipo(),
                url.tipo().rotulo(),
                url.ambienteId(),
                catalogoService.descrever(CatalogoTipo.AMBIENTE, url.ambienteId()),
                url.acessoId(),
                catalogoService.descrever(CatalogoTipo.ACESSO, url.acessoId()),
                url.frameworkId(),
                catalogoService.descrever(CatalogoTipo.FRAMEWORK, url.frameworkId()),
                url.autenticacaoId(),
                catalogoService.descrever(CatalogoTipo.AUTENTICACAO, url.autenticacaoId()),
                url.registroAtivo());
    }

    private Url.Dados extrairDados(UrlRequest request) {
        if (request.ambienteId() == null || request.acessoId() == null
                || request.frameworkId() == null || request.autenticacaoId() == null) {
            throw new RegraDeNegocioException("Ambiente, acesso, framework e autenticacao sao obrigatorios");
        }
        catalogoService.buscar(CatalogoTipo.AMBIENTE, request.ambienteId());
        catalogoService.buscar(CatalogoTipo.ACESSO, request.acessoId());
        catalogoService.buscar(CatalogoTipo.FRAMEWORK, request.frameworkId());
        catalogoService.buscar(CatalogoTipo.AUTENTICACAO, request.autenticacaoId());

        return new Url.Dados(request.endereco(),
                request.ambienteId(),
                request.acessoId(),
                request.frameworkId(),
                request.autenticacaoId());
    }
}