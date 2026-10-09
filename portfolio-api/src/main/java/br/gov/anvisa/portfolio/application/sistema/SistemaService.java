package br.gov.anvisa.portfolio.application.sistema;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.anvisa.portfolio.application.catalogo.CatalogoService;
import br.gov.anvisa.portfolio.application.sistema.dto.SistemaRequest;
import br.gov.anvisa.portfolio.application.sistema.dto.SistemaResponse;
import br.gov.anvisa.portfolio.domain.catalogo.CatalogoTipo;
import br.gov.anvisa.portfolio.domain.shared.Pagina;
import br.gov.anvisa.portfolio.domain.shared.RecursoNaoEncontradoException;
import br.gov.anvisa.portfolio.domain.shared.RegistroDuplicadoException;
import br.gov.anvisa.portfolio.domain.sistema.Sistema;
import br.gov.anvisa.portfolio.domain.sistema.SistemaFiltro;
import br.gov.anvisa.portfolio.domain.sistema.SistemaPort;

/**
 * Casos de uso do agregado Sistema.
 *
 * <p>Agregados filhos (componentes e documentacao) possuem servicos proprios, acessados
 * pelos controladores de sub-recursos.
 */
@Service
public class SistemaService {

    private final SistemaPort sistemaPort;
    private final CatalogoService catalogoService;

    public SistemaService(SistemaPort sistemaPort, CatalogoService catalogoService) {
        this.sistemaPort = sistemaPort;
        this.catalogoService = catalogoService;
    }

    @Transactional(readOnly = true)
    public Pagina<SistemaResponse> listar(SistemaFiltro filtro) {
        return sistemaPort.listar(filtro).map(this::paraResponse);
    }

    @Transactional(readOnly = true)
    public SistemaResponse buscar(Long id) {
        return paraResponse(obterObrigatorio(id));
    }

    @Transactional
    public SistemaResponse criar(SistemaRequest request) {
        validarReferencias(request);
        Sistema.Dados dados = extrairDados(request);
        garantirSiglaLivre(dados.sigla(), null);

        Sistema sistema = Sistema.criar(dados);
        aplicarSituacao(sistema, request);
        return paraResponse(sistemaPort.inserir(sistema));
    }

    @Transactional
    public SistemaResponse alterar(Long id, SistemaRequest request) {
        Sistema sistema = obterObrigatorio(id);
        validarReferencias(request);
        Sistema.Dados dados = extrairDados(request);
        garantirSiglaLivre(dados.sigla(), id);

        sistema.atualizarDados(dados);
        aplicarSituacao(sistema, request);
        return paraResponse(sistemaPort.alterar(sistema));
    }

    @Transactional
    public SistemaResponse validar(Long id) {
        Sistema sistema = obterObrigatorio(id);
        sistema.validar();
        return paraResponse(sistemaPort.alterar(sistema));
    }

    @Transactional
    public SistemaResponse alterarAtivo(Long id, boolean ativo) {
        Sistema sistema = obterObrigatorio(id);
        if (ativo) {
            sistema.ativar();
        } else {
            sistema.desativar();
        }
        return paraResponse(sistemaPort.alterar(sistema));
    }

    @Transactional
    public void excluir(Long id) {
        obterObrigatorio(id);
        sistemaPort.excluir(id);
    }

    public Sistema obterObrigatorio(Long id) {
        return sistemaPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sistema", id));
    }

    private void aplicarSituacao(Sistema sistema, SistemaRequest request) {
        if (request.registroAtivo()) {
            sistema.ativar();
        } else {
            sistema.desativar();
        }
        if (request.validado()) {
            sistema.validar();
        }
    }

    private void validarReferencias(SistemaRequest request) {
        if (request.origemId() != null) {
            catalogoService.buscar(CatalogoTipo.ORIGEM, request.origemId());
        }
        if (request.baseDeDadoId() != null) {
            catalogoService.buscar(CatalogoTipo.BASE_DE_DADO, request.baseDeDadoId());
        }
    }

    private void garantirSiglaLivre(String sigla, Long idIgnorado) {
        sistemaPort.buscarPorSigla(sigla)
                .filter(existente -> !Objects.equals(existente.id(), idIgnorado))
                .ifPresent(existente -> {
                    throw new RegistroDuplicadoException("Ja existe um sistema com a sigla %s".formatted(sigla));
                });
    }

    private Sistema.Dados extrairDados(SistemaRequest request) {
        return new Sistema.Dados(request.sigla(),
                request.nome(),
                request.origemId(),
                request.objetivo(),
                request.arquivo(),
                request.anvisa(),
                request.integracaoContinua(),
                request.classificacaoBem(),
                request.baseDeDadoId());
    }

    SistemaResponse paraResponse(Sistema sistema) {
        return new SistemaResponse(sistema.id(),
                sistema.sigla(),
                sistema.nome(),
                sistema.origemId(),
                catalogoService.descrever(CatalogoTipo.ORIGEM, sistema.origemId()),
                sistema.objetivo(),
                sistema.arquivo(),
                sistema.anvisa(),
                sistema.integracaoContinua(),
                sistema.classificacaoBem(),
                sistema.baseDeDadoId(),
                catalogoService.descrever(CatalogoTipo.BASE_DE_DADO, sistema.baseDeDadoId()),
                sistema.registroAtivo(),
                sistema.validado());
    }
}