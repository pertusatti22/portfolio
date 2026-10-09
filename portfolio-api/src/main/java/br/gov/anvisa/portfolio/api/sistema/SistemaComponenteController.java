package br.gov.anvisa.portfolio.api.sistema;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.anvisa.portfolio.api.common.PageResponse;
import br.gov.anvisa.portfolio.api.common.Paginas;
import br.gov.anvisa.portfolio.application.componente.ComponenteService;
import br.gov.anvisa.portfolio.application.componente.dto.ComponenteRequest;
import br.gov.anvisa.portfolio.application.componente.dto.ComponenteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Endpoints dos componentes de um sistema (sub-recurso de Sistema).
 */
@Tag(name = "Sistemas - Componentes", description = "Componentes de um sistema")
@RestController
@RequestMapping("${portfolio.api.base-path}/sistemas/{sistemaId}/componentes")
public class SistemaComponenteController {

    private final ComponenteService componenteService;
    private final String basePath;

    public SistemaComponenteController(ComponenteService componenteService,
                                       @Value("${portfolio.api.base-path}") String basePath) {
        this.componenteService = componenteService;
        this.basePath = basePath;
    }

    @GetMapping
    @Operation(summary = "Lista os componentes de um sistema")
    public PageResponse<ComponenteResponse> listar(@PathVariable Long sistemaId,
                                                   @RequestParam(required = false) Boolean ativo,
                                                   @RequestParam(required = false) Integer pagina,
                                                   @RequestParam(required = false) Integer tamanho,
                                                   @RequestParam(required = false) String ordenacao,
                                                   @RequestParam(required = false) Boolean crescente) {
        return PageResponse.de(componenteService.listarPorSistema(sistemaId,
                ativo,
                Paginas.requisicao(pagina, tamanho, ordenacao, crescente)));
    }

    @GetMapping("/{componenteId}")
    @Operation(summary = "Consulta um componente de um sistema")
    public ComponenteResponse buscar(@PathVariable Long sistemaId, @PathVariable Long componenteId) {
        return componenteService.buscar(sistemaId, componenteId);
    }

    @PostMapping
    @Operation(summary = "Cria um componente em um sistema")
    public ResponseEntity<ComponenteResponse> criar(@PathVariable Long sistemaId,
                                                    @Valid @RequestBody ComponenteRequest request) {
        ComponenteResponse criado = componenteService.criar(sistemaId, request);
        return ResponseEntity.created(URI.create("%s/sistemas/%d/componentes/%d"
                .formatted(basePath, sistemaId, criado.id()))).body(criado);
    }

    @PutMapping("/{componenteId}")
    @Operation(summary = "Altera um componente de um sistema")
    public ComponenteResponse alterar(@PathVariable Long sistemaId,
                                      @PathVariable Long componenteId,
                                      @Valid @RequestBody ComponenteRequest request) {
        return componenteService.alterar(sistemaId, componenteId, request);
    }

    @DeleteMapping("/{componenteId}")
    @Operation(summary = "Exclui um componente de um sistema")
    public ResponseEntity<Void> excluir(@PathVariable Long sistemaId, @PathVariable Long componenteId) {
        componenteService.excluir(sistemaId, componenteId);
        return ResponseEntity.noContent().build();
    }
}