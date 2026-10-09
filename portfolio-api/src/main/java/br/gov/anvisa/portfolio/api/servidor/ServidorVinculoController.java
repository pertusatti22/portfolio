package br.gov.anvisa.portfolio.api.servidor;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gov.anvisa.portfolio.application.backup.dto.BackupResponse;
import br.gov.anvisa.portfolio.application.catalogo.dto.CatalogoResponse;
import br.gov.anvisa.portfolio.application.servidor.ServidorVinculosService;
import br.gov.anvisa.portfolio.application.sistema.dto.VinculosRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Endpoints dos vinculos de um servidor (bases de dado e backups).
 */
@Tag(name = "Servidores - Vinculos", description = "Relacionamentos de um servidor")
@RestController
@RequestMapping("${portfolio.api.base-path}/servidores/{servidorId}")
public class ServidorVinculoController {

    private final ServidorVinculosService vinculosService;

    public ServidorVinculoController(ServidorVinculosService vinculosService) {
        this.vinculosService = vinculosService;
    }

    @GetMapping("/bases-de-dado")
    @Operation(summary = "Lista as bases de dado vinculadas")
    public List<CatalogoResponse> listarBasesDeDado(@PathVariable Long servidorId) {
        return vinculosService.listarBasesDeDado(servidorId);
    }

    @PutMapping("/bases-de-dado")
    @Operation(summary = "Substitui as bases de dado vinculadas")
    public List<CatalogoResponse> substituirBasesDeDado(@PathVariable Long servidorId,
                                                        @Valid @RequestBody VinculosRequest request) {
        return vinculosService.substituirBasesDeDado(servidorId, request.ids());
    }

    @GetMapping("/backups")
    @Operation(summary = "Lista os identificadores de backup vinculados")
    public List<Long> listarBackups(@PathVariable Long servidorId) {
        return vinculosService.listarBackups(servidorId);
    }

    @GetMapping("/backups/detalhado")
    @Operation(summary = "Lista os backups vinculados com seus atributos")
    public List<BackupResponse> listarBackupsDetalhado(@PathVariable Long servidorId) {
        return vinculosService.listarBackupsDetalhado(servidorId);
    }

    @PutMapping("/backups")
    @Operation(summary = "Substitui os backups vinculados")
    public List<Long> substituirBackups(@PathVariable Long servidorId, @Valid @RequestBody VinculosRequest request) {
        return vinculosService.substituirBackups(servidorId, request.ids());
    }
}