package br.gov.anvisa.portfolio.application.sistema;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.anvisa.portfolio.application.catalogo.dto.CatalogoResponse;
import br.gov.anvisa.portfolio.application.componente.ComponenteService;
import br.gov.anvisa.portfolio.application.componente.dto.ComponenteResponse;
import br.gov.anvisa.portfolio.application.documentacao.DocumentacaoService;
import br.gov.anvisa.portfolio.application.documentacao.dto.DocumentacaoResponse;
import br.gov.anvisa.portfolio.application.servidor.ServidorService;
import br.gov.anvisa.portfolio.application.servidor.dto.ServidorResponse;
import br.gov.anvisa.portfolio.application.sistema.dto.SistemaDetalheResponse;
import br.gov.anvisa.portfolio.application.url.UrlService;
import br.gov.anvisa.portfolio.application.url.dto.UrlResponse;
import br.gov.anvisa.portfolio.domain.shared.PaginaRequest;
import br.gov.anvisa.portfolio.domain.url.UrlFiltro;

/**
 * Composicao da visao completa de um sistema, reunindo o cadastro e seus relacionamentos.
 *
 * <p>Servico separado de {@link SistemaService} para manter a composicao de leitura fora do
 * caso de uso de alteracao do agregado.
 */
@Service
public class SistemaDetalheService {

    private static final PaginaRequest LIMITE = new PaginaRequest(0, PaginaRequest.TAMANHO_MAXIMO, null, true);

    private final SistemaService sistemaService;
    private final ComponenteService componenteService;
    private final DocumentacaoService documentacaoService;
    private final SistemaVinculosService vinculosService;
    private final ServidorService servidorService;
    private final UrlService urlService;

    public SistemaDetalheService(SistemaService sistemaService,
                                 ComponenteService componenteService,
                                 DocumentacaoService documentacaoService,
                                 SistemaVinculosService vinculosService,
                                 ServidorService servidorService,
                                 UrlService urlService) {
        this.sistemaService = sistemaService;
        this.componenteService = componenteService;
        this.documentacaoService = documentacaoService;
        this.vinculosService = vinculosService;
        this.servidorService = servidorService;
        this.urlService = urlService;
    }

    /**
     * Monta a visao completa do sistema, com listas limitadas ao tamanho maximo de pagina.
     */
    @Transactional(readOnly = true)
    public SistemaDetalheResponse buscar(Long sistemaId) {
        List<ComponenteResponse> componentes = componenteService
                .listarPorSistema(sistemaId, Boolean.TRUE, LIMITE)
                .itens();
        List<DocumentacaoResponse> documentacoes = documentacaoService
                .listarPorSistema(sistemaId, null, Boolean.TRUE, LIMITE)
                .itens();
        List<ServidorResponse> servidores = servidorService.listarPorIds(vinculosService.listarServidores(sistemaId));
        List<UrlResponse> urls = urlService.listar(new UrlFiltro(null, sistemaId, null, null, Boolean.TRUE, LIMITE))
                .itens();
        List<CatalogoResponse> softwares = vinculosService.listarSoftwares(sistemaId);

        return new SistemaDetalheResponse(sistemaService.buscar(sistemaId),
                componentes,
                documentacoes,
                vinculosService.listarSchemas(sistemaId),
                softwares,
                servidores,
                urls);
    }
}