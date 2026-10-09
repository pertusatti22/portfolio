package br.gov.anvisa.portfolio.application.usuario;

import java.time.Clock;
import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.anvisa.portfolio.application.catalogo.CatalogoService;
import br.gov.anvisa.portfolio.application.usuario.dto.UsuarioHistoricoResponse;
import br.gov.anvisa.portfolio.application.usuario.dto.UsuarioRequest;
import br.gov.anvisa.portfolio.application.usuario.dto.UsuarioResponse;
import br.gov.anvisa.portfolio.domain.catalogo.CatalogoTipo;
import br.gov.anvisa.portfolio.domain.shared.Pagina;
import br.gov.anvisa.portfolio.domain.shared.RecursoNaoEncontradoException;
import br.gov.anvisa.portfolio.domain.usuario.Usuario;
import br.gov.anvisa.portfolio.domain.usuario.UsuarioFiltro;
import br.gov.anvisa.portfolio.domain.usuario.UsuarioHistorico;
import br.gov.anvisa.portfolio.domain.usuario.UsuarioHistoricoFiltro;
import br.gov.anvisa.portfolio.domain.usuario.UsuarioPort;

/**
 * Casos de uso do agregado Usuario.
 *
 * <p>O historico de vigencias (TB_HIST_USUARIO) e alimentado por trigger do proprio banco,
 * sendo apenas consultado por esta aplicacao.
 */
@Service
public class UsuarioService {

    private final UsuarioPort usuarioPort;
    private final CatalogoService catalogoService;
    private final Clock clock;

    public UsuarioService(UsuarioPort usuarioPort, CatalogoService catalogoService, Clock clock) {
        this.usuarioPort = usuarioPort;
        this.catalogoService = catalogoService;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    public Pagina<UsuarioResponse> listar(UsuarioFiltro filtro) {
        return usuarioPort.listar(filtro).map(this::paraResponse);
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscar(String username) {
        return paraResponse(obterObrigatorio(username));
    }

    @Transactional
    public UsuarioResponse criar(UsuarioRequest request) {
        validarReferencias(request);
        Usuario usuario = new Usuario(request.username(),
                request.host(),
                request.tipoUsuarioId(),
                request.inicioVigencia(),
                request.fimVigencia(),
                request.usuarioBancoDadosId(),
                request.usuarioSecundario());
        return paraResponse(usuarioPort.inserir(usuario));
    }

    @Transactional
    public UsuarioResponse alterar(String username, UsuarioRequest request) {
        Usuario existente = obterObrigatorio(username);
        validarReferencias(request);
        Usuario usuario = new Usuario(existente.username(),
                request.host(),
                request.tipoUsuarioId(),
                request.inicioVigencia(),
                request.fimVigencia(),
                request.usuarioBancoDadosId(),
                request.usuarioSecundario());
        return paraResponse(usuarioPort.alterar(usuario));
    }

    @Transactional
    public UsuarioResponse inativar(String username, LocalDate dataFim) {
        Usuario existente = obterObrigatorio(username);
        LocalDate referencia = dataFim == null ? hoje() : dataFim;
        Usuario usuario = existente.comVigencia(existente.inicioVigencia(), referencia);
        return paraResponse(usuarioPort.alterar(usuario));
    }

    @Transactional
    public void excluir(String username) {
        obterObrigatorio(username);
        usuarioPort.excluir(username);
    }

    @Transactional(readOnly = true)
    public Pagina<UsuarioHistoricoResponse> listarHistorico(UsuarioHistoricoFiltro filtro) {
        return usuarioPort.listarHistorico(filtro).map(this::paraHistoricoResponse);
    }

    public Usuario obterObrigatorio(String username) {
        return usuarioPort.buscarPorUsername(username)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario", username));
    }

    private void validarReferencias(UsuarioRequest request) {
        catalogoService.buscar(CatalogoTipo.TIPO_USUARIO, request.tipoUsuarioId());
        catalogoService.buscar(CatalogoTipo.USUARIO_BANCO_DADOS, request.usuarioBancoDadosId());
    }

    private LocalDate hoje() {
        return LocalDate.now(clock);
    }

    private UsuarioResponse paraResponse(Usuario usuario) {
        return new UsuarioResponse(usuario.username(),
                usuario.host(),
                usuario.tipoUsuarioId(),
                catalogoService.descrever(CatalogoTipo.TIPO_USUARIO, usuario.tipoUsuarioId()),
                usuario.inicioVigencia(),
                usuario.fimVigencia(),
                usuario.usuarioBancoDadosId(),
                catalogoService.descrever(CatalogoTipo.USUARIO_BANCO_DADOS, usuario.usuarioBancoDadosId()),
                usuario.usuarioSecundario(),
                usuario.vigente(hoje()));
    }

    private UsuarioHistoricoResponse paraHistoricoResponse(UsuarioHistorico historico) {
        return new UsuarioHistoricoResponse(historico.username(),
                historico.host(),
                historico.tipoUsuarioId(),
                catalogoService.descrever(CatalogoTipo.TIPO_USUARIO, historico.tipoUsuarioId()),
                historico.inicioVigencia(),
                historico.fimVigencia(),
                historico.usuarioBancoDadosId(),
                catalogoService.descrever(CatalogoTipo.USUARIO_BANCO_DADOS, historico.usuarioBancoDadosId()),
                historico.usuarioAplicacao(),
                historico.dataRegistro(),
                historico.operacao());
    }
}