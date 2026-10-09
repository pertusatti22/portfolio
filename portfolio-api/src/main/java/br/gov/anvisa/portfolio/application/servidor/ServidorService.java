package br.gov.anvisa.portfolio.application.servidor;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.anvisa.portfolio.application.catalogo.CatalogoService;
import br.gov.anvisa.portfolio.application.servidor.dto.ServidorRequest;
import br.gov.anvisa.portfolio.application.servidor.dto.ServidorResponse;
import br.gov.anvisa.portfolio.domain.catalogo.CatalogoTipo;
import br.gov.anvisa.portfolio.domain.servidor.Servidor;
import br.gov.anvisa.portfolio.domain.servidor.ServidorFiltro;
import br.gov.anvisa.portfolio.domain.servidor.ServidorPort;
import br.gov.anvisa.portfolio.domain.shared.Pagina;
import br.gov.anvisa.portfolio.domain.shared.RecursoNaoEncontradoException;
import br.gov.anvisa.portfolio.domain.shared.RegistroDuplicadoException;

/**
 * Casos de uso do agregado Servidor.
 */
@Service
public class ServidorService {

    private final ServidorPort servidorPort;
    private final CatalogoService catalogoService;

    public ServidorService(ServidorPort servidorPort, CatalogoService catalogoService) {
        this.servidorPort = servidorPort;
        this.catalogoService = catalogoService;
    }

    @Transactional(readOnly = true)
    public Pagina<ServidorResponse> listar(ServidorFiltro filtro) {
        return servidorPort.listar(filtro).map(this::paraResponse);
    }

    @Transactional(readOnly = true)
    public ServidorResponse buscar(Long id) {
        return paraResponse(obterObrigatorio(id));
    }

    @Transactional
    public ServidorResponse criar(ServidorRequest request) {
        Servidor.Dados dados = extrairDados(request);
        garantirNomeLivre(dados.nome(), null);
        return paraResponse(servidorPort.inserir(Servidor.criar(dados)));
    }

    @Transactional
    public ServidorResponse alterar(Long id, ServidorRequest request) {
        Servidor servidor = obterObrigatorio(id);
        Servidor.Dados dados = extrairDados(request);
        garantirNomeLivre(dados.nome(), id);

        servidor.atualizarDados(dados);
        return paraResponse(servidorPort.alterar(servidor));
    }

    @Transactional
    public ServidorResponse alterarLigado(Long id, boolean ligado) {
        Servidor servidor = obterObrigatorio(id);
        Servidor.Dados dados = new Servidor.Dados(servidor.nome(), ligado, servidor.cpu(), servidor.memoriaGb(),
                servidor.sistemaOperacionalId(), servidor.tamanhoGb(), servidor.armazenamentoId(),
                servidor.ambienteId(), servidor.aplicacao());
        servidor.atualizarDados(dados);
        return paraResponse(servidorPort.alterar(servidor));
    }

    @Transactional
    public void excluir(Long id) {
        obterObrigatorio(id);
        servidorPort.excluir(id);
    }

    @Transactional(readOnly = true)
    public List<ServidorResponse> listarPorIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return servidorPort.listarPorIds(ids).stream().map(this::paraResponse).toList();
    }

    public Servidor obterObrigatorio(Long id) {
        return servidorPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Servidor", id));
    }

    ServidorResponse paraResponse(Servidor servidor) {
        return new ServidorResponse(servidor.id(),
                servidor.nome(),
                servidor.ligado(),
                servidor.cpu(),
                servidor.memoriaGb(),
                servidor.sistemaOperacionalId(),
                catalogoService.descrever(CatalogoTipo.SISTEMA_OPERACIONAL, servidor.sistemaOperacionalId()),
                servidor.tamanhoGb(),
                servidor.armazenamentoId(),
                catalogoService.descrever(CatalogoTipo.ARMAZENAMENTO, servidor.armazenamentoId()),
                servidor.ambienteId(),
                catalogoService.descrever(CatalogoTipo.AMBIENTE, servidor.ambienteId()),
                servidor.aplicacao(),
                servidor.possuiBackup());
    }

    private void garantirNomeLivre(String nome, Long idIgnorado) {
        servidorPort.buscarPorNome(nome)
                .filter(existente -> !Objects.equals(existente.id(), idIgnorado))
                .ifPresent(existente -> {
                    throw new RegistroDuplicadoException("Ja existe um servidor com o nome %s".formatted(nome));
                });
    }

    private Servidor.Dados extrairDados(ServidorRequest request) {
        return new Servidor.Dados(request.nome(),
                request.ligado(),
                request.cpu(),
                request.memoriaGb(),
                request.sistemaOperacionalId(),
                request.tamanhoGb(),
                request.armazenamentoId(),
                request.ambienteId(),
                request.aplicacao());
    }
}