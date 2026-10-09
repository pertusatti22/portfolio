package br.gov.anvisa.portfolio.api.usuario;

import java.net.URI;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
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
import br.gov.anvisa.portfolio.application.usuario.UsuarioService;
import br.gov.anvisa.portfolio.application.usuario.dto.UsuarioHistoricoResponse;
import br.gov.anvisa.portfolio.application.usuario.dto.UsuarioRequest;
import br.gov.anvisa.portfolio.application.usuario.dto.UsuarioResponse;
import br.gov.anvisa.portfolio.domain.usuario.TipoOperacao;
import br.gov.anvisa.portfolio.domain.usuario.UsuarioFiltro;
import br.gov.anvisa.portfolio.domain.usuario.UsuarioHistoricoFiltro;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Endpoints do agregado Usuario e do seu historico de vigencias.
 */
@Tag(name = "Usuarios", description = "Usuarios do portifolio e historico de vigencia")
@RestController
@RequestMapping("${portfolio.api.base-path}/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final String basePath;

    public UsuarioController(UsuarioService usuarioService, @Value("${portfolio.api.base-path}") String basePath) {
        this.usuarioService = usuarioService;
        this.basePath = basePath;
    }

    @GetMapping
    @Operation(summary = "Lista os usuarios")
    public PageResponse<UsuarioResponse> listar(@RequestParam(required = false) String termo,
                                                @RequestParam(required = false) Long tipoUsuarioId,
                                                @RequestParam(required = false) Long usuarioBancoDadosId,
                                                @RequestParam(required = false) Boolean somenteVigentes,
                                                @RequestParam(required = false) Integer pagina,
                                                @RequestParam(required = false) Integer tamanho,
                                                @RequestParam(required = false) String ordenacao,
                                                @RequestParam(required = false) Boolean crescente) {
        UsuarioFiltro filtro = new UsuarioFiltro(termo,
                tipoUsuarioId,
                usuarioBancoDadosId,
                somenteVigentes,
                Paginas.requisicao(pagina, tamanho, ordenacao, crescente));
        return PageResponse.de(usuarioService.listar(filtro));
    }

    @GetMapping("/historico")
    @Operation(summary = "Lista o historico de vigencia gravado pelo banco")
    public PageResponse<UsuarioHistoricoResponse> listarHistorico(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Long tipoUsuarioId,
            @RequestParam(required = false) TipoOperacao operacao,
            @RequestParam(required = false) Integer pagina,
            @RequestParam(required = false) Integer tamanho,
            @RequestParam(required = false) String ordenacao,
            @RequestParam(required = false) Boolean crescente) {
        UsuarioHistoricoFiltro filtro = new UsuarioHistoricoFiltro(username,
                tipoUsuarioId,
                operacao,
                Paginas.requisicao(pagina, tamanho, ordenacao, crescente));
        return PageResponse.de(usuarioService.listarHistorico(filtro));
    }

    @GetMapping("/{username}")
    @Operation(summary = "Consulta um usuario")
    public UsuarioResponse buscar(@PathVariable String username) {
        return usuarioService.buscar(username);
    }

    @PostMapping
    @Operation(summary = "Cria um usuario")
    public ResponseEntity<UsuarioResponse> criar(@Valid @RequestBody UsuarioRequest request) {
        UsuarioResponse criado = usuarioService.criar(request);
        return ResponseEntity.created(URI.create("%s/usuarios/%s".formatted(basePath, criado.username())))
                .body(criado);
    }

    @PutMapping("/{username}")
    @Operation(summary = "Altera um usuario")
    public UsuarioResponse alterar(@PathVariable String username, @Valid @RequestBody UsuarioRequest request) {
        return usuarioService.alterar(username, request);
    }

    @PatchMapping("/{username}/inativar")
    @Operation(summary = "Encerra a vigencia de um usuario")
    public UsuarioResponse inativar(@PathVariable String username,
                                    @RequestParam(required = false)
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        return usuarioService.inativar(username, dataFim);
    }

    @DeleteMapping("/{username}")
    @Operation(summary = "Exclui um usuario")
    public ResponseEntity<Void> excluir(@PathVariable String username) {
        usuarioService.excluir(username);
        return ResponseEntity.noContent().build();
    }
}