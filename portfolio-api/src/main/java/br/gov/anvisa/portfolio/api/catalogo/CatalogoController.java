package br.gov.anvisa.portfolio.api.catalogo;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

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
import br.gov.anvisa.portfolio.application.catalogo.CatalogoService;
import br.gov.anvisa.portfolio.application.catalogo.dto.CatalogoRequest;
import br.gov.anvisa.portfolio.application.catalogo.dto.CatalogoResponse;
import br.gov.anvisa.portfolio.domain.catalogo.CatalogoTipo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Endpoints dos catalogos de dados de referencia (tabelas TD_*).
 *
 * <p>Todos os catalogos possuem a mesma estrutura, por isso o recurso e discriminado
 * pelo segmento {@code {recurso}} da URL (ex.: {@code /catalogos/origens}).
 */
@Tag(name = "Catalogos", description = "Catalogos de dados de referencia")
@RestController
@RequestMapping("${portfolio.api.base-path}/catalogos")
public class CatalogoController {

    private final CatalogoService catalogoService;
    private final String basePath;

    public CatalogoController(CatalogoService catalogoService,
                              @Value("${portfolio.api.base-path}") String basePath) {
        this.catalogoService = catalogoService;
        this.basePath = basePath;
    }

    @GetMapping
    @Operation(summary = "Lista os catalogos disponiveis")
    public List<RecursoCatalogoResponse> listarRecursos() {
        return Arrays.stream(CatalogoTipo.values()).map(CatalogoController::paraRecurso).toList();
    }

    @GetMapping("/{recurso}")
    @Operation(summary = "Lista os itens de um catalogo")
    public PageResponse<CatalogoResponse> listar(
            @Parameter(description = "Catalogo desejado") @PathVariable String recurso,
            @RequestParam(required = false) String filtro,
            @RequestParam(defaultValue = "true") boolean somenteAtivos,
            @RequestParam(required = false) Integer pagina,
            @RequestParam(required = false) Integer tamanho,
            @RequestParam(required = false) String ordenacao,
            @RequestParam(required = false) Boolean crescente) {
        return PageResponse.de(catalogoService.listar(doCatalogo(recurso),
                filtro,
                somenteAtivos,
                Paginas.requisicao(pagina, tamanho, ordenacao, crescente)));
    }

    @GetMapping("/{recurso}/todos")
    @Operation(summary = "Lista integralmente um catalogo, util para listas suspensas")
    public List<CatalogoResponse> listarTodos(
            @PathVariable String recurso,
            @RequestParam(defaultValue = "true") boolean somenteAtivos) {
        return catalogoService.listarTodos(doCatalogo(recurso), somenteAtivos);
    }

    @GetMapping("/{recurso}/{id}")
    @Operation(summary = "Consulta um item de catalogo")
    public CatalogoResponse buscar(@PathVariable String recurso, @PathVariable Long id) {
        return catalogoService.buscar(doCatalogo(recurso), id);
    }

    @PostMapping("/{recurso}")
    @Operation(summary = "Cria um item de catalogo")
    public ResponseEntity<CatalogoResponse> criar(@PathVariable String recurso,
                                                  @Valid @RequestBody CatalogoRequest request) {
        CatalogoResponse criado = catalogoService.criar(doCatalogo(recurso), request);
        return ResponseEntity.created(URI.create("%s/catalogos/%s/%d".formatted(basePath, recurso, criado.id())))
                .body(criado);
    }

    @PutMapping("/{recurso}/{id}")
    @Operation(summary = "Altera um item de catalogo")
    public CatalogoResponse alterar(@PathVariable String recurso,
                                    @PathVariable Long id,
                                    @Valid @RequestBody CatalogoRequest request) {
        return catalogoService.alterar(doCatalogo(recurso), id, request);
    }

    @PatchMapping("/{recurso}/{id}/desativar")
    @Operation(summary = "Desativa um item de catalogo")
    public ResponseEntity<Void> desativar(@PathVariable String recurso, @PathVariable Long id) {
        catalogoService.desativar(doCatalogo(recurso), id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{recurso}/{id}")
    @Operation(summary = "Exclui um item de catalogo")
    public ResponseEntity<Void> excluir(@PathVariable String recurso, @PathVariable Long id) {
        catalogoService.excluir(doCatalogo(recurso), id);
        return ResponseEntity.noContent().build();
    }

    private CatalogoTipo doCatalogo(String recurso) {
        return CatalogoTipo.doRecurso(recurso);
    }

    private static RecursoCatalogoResponse paraRecurso(CatalogoTipo tipo) {
        return new RecursoCatalogoResponse(tipo.recurso(), tipo.rotulo(), tipo.possuiVersao(),
                tipo.possuiIndicadorLiberacao(), tipo.tamanhoDescricao());
    }

    /**
     * Descricao de um catalogo disponivel, usada pelo front para montar o menu de cadastros.
     */
    public record RecursoCatalogoResponse(String recurso,
                                          String rotulo,
                                          boolean possuiVersao,
                                          boolean possuiIndicadorLiberacao,
                                          int tamanhoDescricao) {
    }
}