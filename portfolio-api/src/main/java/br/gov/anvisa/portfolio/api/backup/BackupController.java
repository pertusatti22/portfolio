package br.gov.anvisa.portfolio.api.backup;

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
import br.gov.anvisa.portfolio.application.backup.BackupService;
import br.gov.anvisa.portfolio.application.backup.dto.BackupRequest;
import br.gov.anvisa.portfolio.application.backup.dto.BackupResponse;
import br.gov.anvisa.portfolio.domain.backup.BackupFiltro;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Endpoints do agregado Backup.
 */
@Tag(name = "Backups", description = "Rotinas de backup dos servidores")
@RestController
@RequestMapping("${portfolio.api.base-path}/backups")
public class BackupController {

    private final BackupService backupService;
    private final String basePath;

    public BackupController(BackupService backupService, @Value("${portfolio.api.base-path}") String basePath) {
        this.backupService = backupService;
        this.basePath = basePath;
    }

    @GetMapping
    @Operation(summary = "Lista os backups")
    public PageResponse<BackupResponse> listar(@RequestParam(required = false) Long servidorId,
                                               @RequestParam(required = false) String tipoBackup,
                                               @RequestParam(required = false) String instancia,
                                               @RequestParam(required = false) String termo,
                                               @RequestParam(required = false) Integer pagina,
                                               @RequestParam(required = false) Integer tamanho,
                                               @RequestParam(required = false) String ordenacao,
                                               @RequestParam(required = false) Boolean crescente) {
        BackupFiltro filtro = new BackupFiltro(servidorId,
                tipoBackup,
                instancia,
                termo,
                Paginas.requisicao(pagina, tamanho, ordenacao, crescente));
        return PageResponse.de(backupService.listar(filtro));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulta um backup")
    public BackupResponse buscar(@PathVariable Long id) {
        return backupService.buscar(id);
    }

    @PostMapping
    @Operation(summary = "Cria um backup")
    public ResponseEntity<BackupResponse> criar(@Valid @RequestBody BackupRequest request) {
        BackupResponse criado = backupService.criar(request);
        return ResponseEntity.created(URI.create("%s/backups/%d".formatted(basePath, criado.id()))).body(criado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Altera um backup")
    public BackupResponse alterar(@PathVariable Long id, @Valid @RequestBody BackupRequest request) {
        return backupService.alterar(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um backup")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        backupService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}