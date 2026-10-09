package br.gov.anvisa.portfolio.api.sistema;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gov.anvisa.portfolio.application.catalogo.dto.CatalogoResponse;
import br.gov.anvisa.portfolio.application.sistema.SistemaVinculosService;
import br.gov.anvisa.portfolio.application.sistema.dto.SchemaVinculadoResponse;
import br.gov.anvisa.portfolio.application.sistema.dto.VinculosRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Endpoints dos vinculos de um sistema (schemas, softwares, servidores e urls).
 */
@Tag(name = "Sistemas - Vinculos", description = "Relacionamentos de um sistema")
@RestController
@RequestMapping("${portfolio.api.base-path}/sistemas/{sistemaId}")
public class SistemaVinculoController {

    private final SistemaVinculosService vinculosService;

    public SistemaVinculoController(SistemaVinculosService vinculosService) {
        this.vinculosService = vinculosService;
    }

    @GetMapping("/schemas")
    @Operation(summary = "Lista os schemas vinculados, indicando o principal")
    public List<SchemaVinculadoResponse> listarSchemas(@PathVariable Long sistemaId) {
        return vinculosService.listarSchemas(sistemaId);
    }

    @PutMapping("/schemas")
    @Operation(summary = "Substitui os schemas vinculados de um sistema")
    public List<SchemaVinculadoResponse> substituirSchemas(@PathVariable Long sistemaId,
                                                           @Valid @RequestBody VinculosRequest request) {
        return vinculosService.substituirSchemas(sistemaId, request.ids(), request.principalId());
    }

    @GetMapping("/softwares")
    @Operation(summary = "Lista os softwares vinculados")
    public List<CatalogoResponse> listarSoftwares(@PathVariable Long sistemaId) {
        return vinculosService.listarSoftwares(sistemaId);
    }

    @PutMapping("/softwares")
    @Operation(summary = "Substitui os softwares vinculados de um sistema")
    public List<CatalogoResponse> substituirSoftwares(@PathVariable Long sistemaId,
                                                      @Valid @RequestBody VinculosRequest request) {
        return vinculosService.substituirSoftwares(sistemaId, request.ids());
    }

    @GetMapping("/servidores")
    @Operation(summary = "Lista os servidores vinculados")
    public List<Long> listarServidores(@PathVariable Long sistemaId) {
        return vinculosService.listarServidores(sistemaId);
    }

    @PutMapping("/servidores")
    @Operation(summary = "Substitui os servidores vinculados de um sistema")
    public List<Long> substituirServidores(@PathVariable Long sistemaId,
                                           @Valid @RequestBody VinculosRequest request) {
        return vinculosService.substituirServidores(sistemaId, request.ids());
    }

    @GetMapping("/urls")
    @Operation(summary = "Lista as urls vinculadas")
    public List<Long> listarUrls(@PathVariable Long sistemaId) {
        return vinculosService.listarUrls(sistemaId);
    }

    @PutMapping("/urls")
    @Operation(summary = "Substitui as urls vinculadas de um sistema")
    public List<Long> substituirUrls(@PathVariable Long sistemaId, @Valid @RequestBody VinculosRequest request) {
        return vinculosService.substituirUrls(sistemaId, request.ids());
    }
}