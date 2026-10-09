package br.gov.anvisa.portfolio.application.componente;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.anvisa.portfolio.application.componente.dto.ComponenteRequest;
import br.gov.anvisa.portfolio.application.componente.dto.ComponenteResponse;
import br.gov.anvisa.portfolio.domain.componente.Componente;
import br.gov.anvisa.portfolio.domain.componente.ComponentePort;
import br.gov.anvisa.portfolio.domain.shared.Pagina;
import br.gov.anvisa.portfolio.domain.shared.PaginaRequest;
import br.gov.anvisa.portfolio.domain.shared.RecursoNaoEncontradoException;

/**
 * Casos de uso do agregado Componente (sub-recurso de Sistema).
 */
@Service
public class ComponenteService {

    private final ComponentePort componentePort;

    public ComponenteService(ComponentePort componentePort) {
        this.componentePort = componentePort;
    }

    @Transactional(readOnly = true)
    public Pagina<ComponenteResponse> listarPorSistema(Long sistemaId, Boolean ativo, PaginaRequest paginacao) {
        return componentePort.listarPorSistema(sistemaId, ativo, paginacao).map(this::paraResponse);
    }

    @Transactional(readOnly = true)
    public ComponenteResponse buscar(Long sistemaId, Long componenteId) {
        return paraResponse(obterDoSistema(sistemaId, componenteId));
    }

    @Transactional
    public ComponenteResponse criar(Long sistemaId, ComponenteRequest request) {
        Componente componente = Componente.criar(sistemaId, request.nome(), request.descricao());
        if (!request.registroAtivo()) {
            componente.desativar();
        }
        return paraResponse(componentePort.inserir(componente));
    }

    @Transactional
    public ComponenteResponse alterar(Long sistemaId, Long componenteId, ComponenteRequest request) {
        Componente componente = obterDoSistema(sistemaId, componenteId);
        componente.atualizarDados(request.nome(), request.descricao());
        if (request.registroAtivo()) {
            componente.ativar();
        } else {
            componente.desativar();
        }
        return paraResponse(componentePort.alterar(componente));
    }

    @Transactional
    public void excluir(Long sistemaId, Long componenteId) {
        obterDoSistema(sistemaId, componenteId);
        componentePort.excluir(componenteId);
    }

    private Componente obterDoSistema(Long sistemaId, Long componenteId) {
        return componentePort.buscarPorId(componenteId)
                .filter(componente -> componente.sistemaId().equals(sistemaId))
                .orElseThrow(() -> new RecursoNaoEncontradoException("Componente", componenteId));
    }

    private ComponenteResponse paraResponse(Componente componente) {
        return new ComponenteResponse(componente.id(),
                componente.sistemaId(),
                componente.nome(),
                componente.descricao(),
                componente.registroAtivo());
    }
}