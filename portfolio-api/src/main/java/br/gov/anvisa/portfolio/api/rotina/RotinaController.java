package br.gov.anvisa.portfolio.api.rotina;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.anvisa.portfolio.api.common.PageResponse;
import br.gov.anvisa.portfolio.api.common.Paginas;
import br.gov.anvisa.portfolio.application.rotina.RotinaService;
import br.gov.anvisa.portfolio.application.rotina.dto.RotinaRequest;
import br.gov.anvisa.portfolio.application.rotina.dto.RotinaResponse;
import br.gov.anvisa.portfolio.domain.rotina.RotinaFiltro;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Endpoints do agregado Rotina (agendamentos no banco de dados).
 */
@Tag(name = "Rotinas", description = "Rotinas agendadas nos bancos de dados")
@RestController
@RequestMapping("${portfolio.api.base-path}/rotinas")
public class RotinaController {

    private final RotinaService rotinaService;
    private final String basePath;

    public RotinaController(RotinaService rotinaService, @Value("${portfolio.api.base-path}") String basePath) {
        this.rotinaService = rotinaService;
        this.basePath = basePath;
    }

    @GetMapping
    @Operation(summary = "Lista as rotinas")
    public PageResponse<RotinaResponse> listar(@RequestParam(required = false) String termo,
                                               @RequestParam(required = false) Long schemaId,
                                               @RequestParam(required = false) String owner,
                                               @RequestParam(required = false) Boolean ativo,
                                               @RequestParam(required = false) Integer pagina,
                                               @RequestParam(required = false) Integer tamanho,
                                               @RequestParam(required = false) String ordenacao,
                                               @RequestParam(required = false) Boolean crescente) {
        RotinaFiltro filtro = new RotinaFiltro(termo,
                schemaId,
                owner,
                ativo,
                Paginas.requisicao(pagina, tamanho, ordenacao, crescente));
        return PageResponse.de(rotinaService.listar(filtro));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulta uma rotina")
    public RotinaResponse buscar(@PathVariable Long id) {
        return rotinaService.buscar(id);
    }

    @PostMapping
    @Operation(summary = "Cria uma rotina")
    public ResponseEntity<RotinaResponse> criar(@Valid @RequestBody RotinaRequest request) {
        RotinaResponse criada = rotinaService.criar(request);
        return ResponseEntity.created(URI.create("%s/rotinas/%d".formatted(basePath, criada.id()))).body(criada);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Altera uma rotina")
    public RotinaResponse alterar(@PathVariable Long id, @Valid @RequestBody RotinaRequest request) {
        return rotinaService.alterar(id, request);
    }

    @PatchMapping("/{id}/ativo")
    @Operation(summary = "Ativa ou desativa uma rotina")
    public RotinaResponse alterarAtivo(@PathVariable Long id, @RequestParam boolean ativo) {
        return rotinaService.alterarAtivo(id, ativo);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui uma rotina")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        rotinaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}