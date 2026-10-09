package br.gov.anvisa.portfolio.api.url;

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
import br.gov.anvisa.portfolio.application.url.UrlService;
import br.gov.anvisa.portfolio.application.url.dto.UrlRequest;
import br.gov.anvisa.portfolio.application.url.dto.UrlResponse;
import br.gov.anvisa.portfolio.domain.url.TipoUrl;
import br.gov.anvisa.portfolio.domain.url.UrlFiltro;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Endpoints do agregado URL.
 */
@Tag(name = "URLs", description = "URLs de acesso a sistemas e componentes")
@RestController
@RequestMapping("${portfolio.api.base-path}/urls")
public class UrlController {

    private final UrlService urlService;
    private final String basePath;

    public UrlController(UrlService urlService, @Value("${portfolio.api.base-path}") String basePath) {
        this.urlService = urlService;
        this.basePath = basePath;
    }

    @GetMapping("/tipos")
    @Operation(summary = "Lista os tipos de URL disponiveis")
    public TipoUrlResponse[] listarTipos() {
        return java.util.Arrays.stream(TipoUrl.values())
                .map(tipo -> new TipoUrlResponse(tipo.name(), tipo.rotulo(), tipo.codigo()))
                .toArray(TipoUrlResponse[]::new);
    }

    @GetMapping
    @Operation(summary = "Lista as URLs")
    public PageResponse<UrlResponse> listar(@RequestParam(required = false) String termo,
                                            @RequestParam(required = false) Long sistemaId,
                                            @RequestParam(required = false) TipoUrl tipo,
                                            @RequestParam(required = false) Long ambienteId,
                                            @RequestParam(required = false) Boolean ativo,
                                            @RequestParam(required = false) Integer pagina,
                                            @RequestParam(required = false) Integer tamanho,
                                            @RequestParam(required = false) String ordenacao,
                                            @RequestParam(required = false) Boolean crescente) {
        UrlFiltro filtro = new UrlFiltro(termo,
                sistemaId,
                tipo,
                ambienteId,
                ativo,
                Paginas.requisicao(pagina, tamanho, ordenacao, crescente));
        return PageResponse.de(urlService.listar(filtro));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulta uma URL")
    public UrlResponse buscar(@PathVariable Long id) {
        return urlService.buscar(id);
    }

    @PostMapping
    @Operation(summary = "Cria uma URL")
    public ResponseEntity<UrlResponse> criar(@Valid @RequestBody UrlRequest request) {
        UrlResponse criada = urlService.criar(request);
        return ResponseEntity.created(URI.create("%s/urls/%d".formatted(basePath, criada.id()))).body(criada);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Altera uma URL")
    public UrlResponse alterar(@PathVariable Long id, @Valid @RequestBody UrlRequest request) {
        return urlService.alterar(id, request);
    }

    @PatchMapping("/{id}/ativo")
    @Operation(summary = "Ativa ou desativa uma URL")
    public UrlResponse alterarAtivo(@PathVariable Long id, @RequestParam boolean ativo) {
        return urlService.alterarAtivo(id, ativo);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui uma URL")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        urlService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    public record TipoUrlResponse(String nome, String rotulo, char codigo) {
    }
}