package br.gov.anvisa.portfolio.application.sistema;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.anvisa.portfolio.application.catalogo.CatalogoService;
import br.gov.anvisa.portfolio.application.catalogo.dto.CatalogoResponse;
import br.gov.anvisa.portfolio.application.sistema.dto.SchemaVinculadoResponse;
import br.gov.anvisa.portfolio.domain.catalogo.CatalogoTipo;
import br.gov.anvisa.portfolio.domain.shared.RecursoNaoEncontradoException;
import br.gov.anvisa.portfolio.domain.shared.RegraDeNegocioException;
import br.gov.anvisa.portfolio.domain.sistema.SistemaPort;
import br.gov.anvisa.portfolio.domain.servidor.ServidorPort;
import br.gov.anvisa.portfolio.domain.url.UrlPort;
import br.gov.anvisa.portfolio.domain.vinculo.TipoVinculo;
import br.gov.anvisa.portfolio.domain.vinculo.VinculoPort;

/**
 * Casos de uso dos vinculos do sistema (schemas, urls, softwares e servidores).
 *
 * <p>As tabelas RL_SISTEMA_* sao manipuladas como conjunto: a troca e feita de forma
 * atomica por {@link VinculoPort#substituirVinculos}.
 */
@Service
public class SistemaVinculosService {

    private final VinculoPort vinculoPort;
    private final SistemaPort sistemaPort;
    private final ServidorPort servidorPort;
    private final UrlPort urlPort;
    private final CatalogoService catalogoService;

    public SistemaVinculosService(VinculoPort vinculoPort,
                                  SistemaPort sistemaPort,
                                  ServidorPort servidorPort,
                                  UrlPort urlPort,
                                  CatalogoService catalogoService) {
        this.vinculoPort = vinculoPort;
        this.sistemaPort = sistemaPort;
        this.servidorPort = servidorPort;
        this.urlPort = urlPort;
        this.catalogoService = catalogoService;
    }

    @Transactional(readOnly = true)
    public List<SchemaVinculadoResponse> listarSchemas(Long sistemaId) {
        List<Long> principais = vinculoPort.listarPrincipais(sistemaId, TipoVinculo.SCHEMA);
        List<Long> schemaIds = vinculoPort.listarVinculados(sistemaId, TipoVinculo.SCHEMA);
        return schemaIds.stream()
                .map(schemaId -> new SchemaVinculadoResponse(schemaId,
                        catalogoService.descrever(CatalogoTipo.SCHEMA, schemaId),
                        principais.contains(schemaId)))
                .toList();
    }

    @Transactional
    public List<SchemaVinculadoResponse> substituirSchemas(Long sistemaId, List<Long> schemaIds, Long principalId) {
        schemaIds.forEach(id -> catalogoService.buscar(CatalogoTipo.SCHEMA, id));
        if (principalId != null && !schemaIds.contains(principalId)) {
            throw new RegraDeNegocioException("O schema principal deve pertencer ao conjunto de schemas vinculados");
        }

        substituir(sistemaId, TipoVinculo.SCHEMA, schemaIds);
        vinculoPort.definirPrincipal(sistemaId, TipoVinculo.SCHEMA, principalId);
        return listarSchemas(sistemaId);
    }

    @Transactional(readOnly = true)
    public List<CatalogoResponse> listarSoftwares(Long sistemaId) {
        return vinculoPort.listarVinculados(sistemaId, TipoVinculo.SOFTWARE).stream()
                .map(id -> catalogoService.buscar(CatalogoTipo.SOFTWARE, id))
                .toList();
    }

    @Transactional
    public List<CatalogoResponse> substituirSoftwares(Long sistemaId, List<Long> softwareIds) {
        softwareIds.forEach(id -> catalogoService.buscar(CatalogoTipo.SOFTWARE, id));
        substituir(sistemaId, TipoVinculo.SOFTWARE, softwareIds);
        return listarSoftwares(sistemaId);
    }

    @Transactional(readOnly = true)
    public List<Long> listarServidores(Long sistemaId) {
        return listarIds(sistemaId, TipoVinculo.SERVIDOR);
    }

    @Transactional
    public List<Long> substituirServidores(Long sistemaId, List<Long> servidorIds) {
        servidorIds.forEach(id -> servidorPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Servidor", id)));
        substituir(sistemaId, TipoVinculo.SERVIDOR, servidorIds);
        return listarServidores(sistemaId);
    }

    @Transactional(readOnly = true)
    public List<Long> listarUrls(Long sistemaId) {
        return listarIds(sistemaId, TipoVinculo.URL);
    }

    @Transactional
    public List<Long> substituirUrls(Long sistemaId, List<Long> urlIds) {
        urlIds.forEach(id -> urlPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("URL", id)));
        substituir(sistemaId, TipoVinculo.URL, urlIds);
        return listarUrls(sistemaId);
    }

    private List<Long> listarIds(Long sistemaId, TipoVinculo tipo) {
        sistemaPort.buscarPorId(sistemaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sistema", sistemaId));
        return vinculoPort.listarVinculados(sistemaId, tipo);
    }

    private void substituir(Long sistemaId, TipoVinculo tipo, List<Long> ids) {
        sistemaPort.buscarPorId(sistemaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sistema", sistemaId));
        vinculoPort.substituirVinculos(sistemaId, tipo, ids);
    }
}