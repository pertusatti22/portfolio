package br.gov.anvisa.portfolio.api.servidor;

import java.net.URI;
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
import br.gov.anvisa.portfolio.application.servidor.ServidorService;
import br.gov.anvisa.portfolio.application.servidor.dto.ServidorRequest;
import br.gov.anvisa.portfolio.application.servidor.dto.ServidorResponse;
import br.gov.anvisa.portfolio.domain.servidor.ServidorFiltro;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Endpoints do agregado Servidor.
 */
@Tag(name = "Servidores", description = "Servidores do portifolio")
@RestController
@RequestMapping("${portfolio.api.base-path}/servidores")
public class ServidorController {

    private final ServidorService servidorService;
    private final String basePath;

    public ServidorController(ServidorService servidorService,
                              @Value("${portfolio.api.base-path}") String basePath) {
        this.servidorService = servidorService;
        this.basePath = basePath;
    }

    @GetMapping
    @Operation(summary = "Lista os servidores")
    public PageResponse<ServidorResponse> listar(@RequestParam(required = false) String termo,
                                                 @RequestParam(required = false) Long ambienteId,
                                                 @RequestParam(required = false) Long sistemaOperacionalId,
                                                 @RequestParam(required = false) Boolean ligado,
                                                 @RequestParam(required = false) Integer pagina,
                                                 @RequestParam(required = false) Integer tamanho,
                                                 @RequestParam(required = false) String ordenacao,
                                                 @RequestParam(required = false) Boolean crescente) {
        ServidorFiltro filtro = new ServidorFiltro(termo,
                ambienteId,
                sistemaOperacionalId,
                ligado,
                Paginas.requisicao(pagina, tamanho, ordenacao, crescente));
        return PageResponse.de(servidorService.listar(filtro));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulta um servidor")
    public ServidorResponse buscar(@PathVariable Long id) {
        return servidorService.buscar(id);
    }

    @PostMapping
    @Operation(summary = "Cria um servidor")
    public ResponseEntity<ServidorResponse> criar(@Valid @RequestBody ServidorRequest request) {
        ServidorResponse criado = servidorService.criar(request);
        return ResponseEntity.created(URI.create("%s/servidores/%d".formatted(basePath, criado.id()))).body(criado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Altera um servidor")
    public ServidorResponse alterar(@PathVariable Long id, @Valid @RequestBody ServidorRequest request) {
        return servidorService.alterar(id, request);
    }

    @PatchMapping("/{id}/ligado")
    @Operation(summary = "Liga ou desliga um servidor")
    public ServidorResponse alterarLigado(@PathVariable Long id, @RequestParam boolean ligado) {
        return servidorService.alterarLigado(id, ligado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um servidor")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        servidorService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Consulta varios servidores em uma unica chamada, utilizado pelo front ao montar vinculos.
     */
    @GetMapping("/lista")
    @Operation(summary = "Consulta servidores por identificadores")
    public List<ServidorResponse> listarPorIds(@RequestParam List<Long> ids) {
        return servidorService.listarPorIds(ids);
    }
}