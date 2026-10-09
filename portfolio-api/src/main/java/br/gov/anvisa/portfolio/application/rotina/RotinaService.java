package br.gov.anvisa.portfolio.application.rotina;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.anvisa.portfolio.application.catalogo.CatalogoService;
import br.gov.anvisa.portfolio.application.rotina.dto.RotinaRequest;
import br.gov.anvisa.portfolio.application.rotina.dto.RotinaResponse;
import br.gov.anvisa.portfolio.domain.catalogo.CatalogoTipo;
import br.gov.anvisa.portfolio.domain.rotina.Rotina;
import br.gov.anvisa.portfolio.domain.rotina.RotinaFiltro;
import br.gov.anvisa.portfolio.domain.rotina.RotinaPort;
import br.gov.anvisa.portfolio.domain.shared.Pagina;
import br.gov.anvisa.portfolio.domain.shared.RecursoNaoEncontradoException;
import br.gov.anvisa.portfolio.domain.shared.RegraDeNegocioException;
import br.gov.anvisa.portfolio.domain.shared.RegistroDuplicadoException;

/**
 * Casos de uso do agregado Rotina (jobs agendados por schema).
 */
@Service
public class RotinaService {

    private final RotinaPort rotinaPort;
    private final CatalogoService catalogoService;

    public RotinaService(RotinaPort rotinaPort, CatalogoService catalogoService) {
        this.rotinaPort = rotinaPort;
        this.catalogoService = catalogoService;
    }

    @Transactional(readOnly = true)
    public Pagina<RotinaResponse> listar(RotinaFiltro filtro) {
        return rotinaPort.listar(filtro).map(this::paraResponse);
    }

    @Transactional(readOnly = true)
    public RotinaResponse buscar(Long id) {
        return paraResponse(obterObrigatorio(id));
    }

    @Transactional
    public RotinaResponse criar(RotinaRequest request) {
        validarSchema(request.schemaId());
        garantirJobLivre(request.owner(), request.nomeJob(), null);

        Rotina rotina = Rotina.criar(request.schemaId(), request.owner(), request.nomeJob(), request.intervaloExecucao());
        if (!request.registroAtivo()) {
            rotina.desativar();
        }
        return paraResponse(rotinaPort.inserir(rotina));
    }

    @Transactional
    public RotinaResponse alterar(Long id, RotinaRequest request) {
        Rotina rotina = obterObrigatorio(id);
        validarSchema(request.schemaId());
        garantirJobLivre(request.owner(), request.nomeJob(), id);

        rotina.atualizarDados(request.owner(), request.nomeJob(), request.intervaloExecucao());
        if (request.registroAtivo()) {
            rotina.ativar();
        } else {
            rotina.desativar();
        }
        return paraResponse(rotinaPort.alterar(rotina));
    }

    @Transactional
    public RotinaResponse alterarAtivo(Long id, boolean ativo) {
        Rotina rotina = obterObrigatorio(id);
        if (ativo) {
            rotina.ativar();
        } else {
            rotina.desativar();
        }
        return paraResponse(rotinaPort.alterar(rotina));
    }

    @Transactional
    public void excluir(Long id) {
        obterObrigatorio(id);
        rotinaPort.excluir(id);
    }

    private void validarSchema(Long schemaId) {
        if (schemaId == null) {
            throw new RegraDeNegocioException("O schema e obrigatorio");
        }
        catalogoService.buscar(CatalogoTipo.SCHEMA, schemaId);
    }

    private void garantirJobLivre(String owner, String nomeJob, Long idIgnorado) {
        rotinaPort.buscarPorOwnerENomeJob(owner, nomeJob)
                .filter(existente -> !Objects.equals(existente.id(), idIgnorado))
                .ifPresent(existente -> {
                    throw new RegistroDuplicadoException(
                            "Ja existe a rotina %s para o owner %s".formatted(nomeJob, owner));
                });
    }

    private Rotina obterObrigatorio(Long id) {
        return rotinaPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Rotina", id));
    }

    private RotinaResponse paraResponse(Rotina rotina) {
        return new RotinaResponse(rotina.id(),
                rotina.schemaId(),
                catalogoService.descrever(CatalogoTipo.SCHEMA, rotina.schemaId()),
                rotina.owner(),
                rotina.nomeJob(),
                rotina.intervaloExecucao(),
                rotina.registroAtivo());
    }
}