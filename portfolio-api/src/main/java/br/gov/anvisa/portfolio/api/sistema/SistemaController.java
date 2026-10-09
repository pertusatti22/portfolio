package br.gov.anvisa.portfolio.api.sistema;

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
import br.gov.anvisa.portfolio.application.sistema.SistemaDetalheService;
import br.gov.anvisa.portfolio.application.sistema.SistemaService;
import br.gov.anvisa.portfolio.application.sistema.dto.SistemaDetalheResponse;
import br.gov.anvisa.portfolio.application.sistema.dto.SistemaRequest;
import br.gov.anvisa.portfolio.application.sistema.dto.SistemaResponse;
import br.gov.anvisa.portfolio.domain.sistema.SistemaFiltro;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Endpoints do agregado Sistema.
 */
@Tag(name = "Sistemas", description = "Sistemas do portifolio de TI")
@RestController
@RequestMapping("${portfolio.api.base-path}/sistemas")
public class SistemaController {

    private final SistemaService sistemaService;
    private final SistemaDetalheService detalheService;
    private final String basePath;

    public SistemaController(SistemaService sistemaService,
                             SistemaDetalheService detalheService,
                             @Value("${portfolio.api.base-path}") String basePath) {
        this.sistemaService = sistemaService;
        this.detalheService = detalheService;
        this.basePath = basePath;
    }

    @GetMapping
    @Operation(summary = "Lista os sistemas")
    public PageResponse<SistemaResponse> listar(@RequestParam(required = false) String termo,
                                                @RequestParam(required = false) Long origemId,
                                                @RequestParam(required = false) Long baseDeDadoId,
                                                @RequestParam(required = false) Boolean ativo,
                                                @RequestParam(required = false) Boolean validado,
                                                @RequestParam(required = false) Integer pagina,
                                                @RequestParam(required = false) Integer tamanho,
                                                @RequestParam(required = false) String ordenacao,
                                                @RequestParam(required = false) Boolean crescente) {
        SistemaFiltro filtro = new SistemaFiltro(termo,
                origemId,
                baseDeDadoId,
                ativo,
                validado,
                Paginas.requisicao(pagina, tamanho, ordenacao, crescente));
        return PageResponse.de(sistemaService.listar(filtro));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulta um sistema")
    public SistemaResponse buscar(@PathVariable Long id) {
        return sistemaService.buscar(id);
    }

    @GetMapping("/{id}/detalhe")
    @Operation(summary = "Consulta um sistema com componentes, documentacao e vinculos")
    public SistemaDetalheResponse detalhar(@PathVariable Long id) {
        return detalheService.buscar(id);
    }

    @PostMapping
    @Operation(summary = "Cria um sistema")
    public ResponseEntity<SistemaResponse> criar(@Valid @RequestBody SistemaRequest request) {
        SistemaResponse criado = sistemaService.criar(request);
        return ResponseEntity.created(URI.create("%s/sistemas/%d".formatted(basePath, criado.id()))).body(criado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Altera um sistema")
    public SistemaResponse alterar(@PathVariable Long id, @Valid @RequestBody SistemaRequest request) {
        return sistemaService.alterar(id, request);
    }

    @PatchMapping("/{id}/validar")
    @Operation(summary = "Marca o sistema como validado")
    public SistemaResponse validar(@PathVariable Long id) {
        return sistemaService.validar(id);
    }

    @PatchMapping("/{id}/ativo")
    @Operation(summary = "Ativa ou desativa o sistema")
    public SistemaResponse alterarAtivo(@PathVariable Long id, @RequestParam boolean ativo) {
        return sistemaService.alterarAtivo(id, ativo);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um sistema")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        sistemaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}