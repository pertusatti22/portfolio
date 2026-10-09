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
import br.gov.anvisa.portfolio.application.documentacao.DocumentacaoService;
import br.gov.anvisa.portfolio.application.documentacao.dto.DocumentacaoRequest;
import br.gov.anvisa.portfolio.application.documentacao.dto.DocumentacaoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Endpoints das documentacoes de um sistema (sub-recurso de Sistema).
 */
@Tag(name = "Sistemas - Documentacoes", description = "Documentacao de um sistema")
@RestController
@RequestMapping("${portfolio.api.base-path}/sistemas/{sistemaId}/documentacoes")
public class SistemaDocumentacaoController {

    private final DocumentacaoService documentacaoService;
    private final String basePath;

    public SistemaDocumentacaoController(DocumentacaoService documentacaoService,
                                         @Value("${portfolio.api.base-path}") String basePath) {
        this.documentacaoService = documentacaoService;
        this.basePath = basePath;
    }

    @GetMapping
    @Operation(summary = "Lista as documentacoes de um sistema")
    public PageResponse<DocumentacaoResponse> listar(@PathVariable Long sistemaId,
                                                    @RequestParam(required = false) Long tipoDocumentacaoId,
                                                    @RequestParam(required = false) Boolean ativo,
                                                    @RequestParam(required = false) Integer pagina,
                                                    @RequestParam(required = false) Integer tamanho,
                                                    @RequestParam(required = false) String ordenacao,
                                                    @RequestParam(required = false) Boolean crescente) {
        return PageResponse.de(documentacaoService.listarPorSistema(sistemaId,
                tipoDocumentacaoId,
                ativo,
                Paginas.requisicao(pagina, tamanho, ordenacao, crescente)));
    }

    @GetMapping("/{documentacaoId}")
    @Operation(summary = "Consulta a documentacao de um sistema")
    public DocumentacaoResponse buscar(@PathVariable Long sistemaId, @PathVariable Long documentacaoId) {
        return documentacaoService.buscar(sistemaId, documentacaoId);
    }

    @PostMapping
    @Operation(summary = "Cria uma documentacao em um sistema")
    public ResponseEntity<DocumentacaoResponse> criar(@PathVariable Long sistemaId,
                                                      @Valid @RequestBody DocumentacaoRequest request) {
        DocumentacaoResponse criada = documentacaoService.criar(sistemaId, request);
        return ResponseEntity.created(URI.create("%s/sistemas/%d/documentacoes/%d"
                .formatted(basePath, sistemaId, criada.id()))).body(criada);
    }

    @PutMapping("/{documentacaoId}")
    @Operation(summary = "Altera a documentacao de um sistema")
    public DocumentacaoResponse alterar(@PathVariable Long sistemaId,
                                        @PathVariable Long documentacaoId,
                                        @Valid @RequestBody DocumentacaoRequest request) {
        return documentacaoService.alterar(sistemaId, documentacaoId, request);
    }

    @DeleteMapping("/{documentacaoId}")
    @Operation(summary = "Exclui a documentacao de um sistema")
    public ResponseEntity<Void> excluir(@PathVariable Long sistemaId, @PathVariable Long documentacaoId) {
        documentacaoService.excluir(sistemaId, documentacaoId);
        return ResponseEntity.noContent().build();
    }
}