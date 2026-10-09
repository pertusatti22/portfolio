package br.gov.anvisa.portfolio.application.catalogo;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.anvisa.portfolio.application.catalogo.dto.CatalogoRequest;
import br.gov.anvisa.portfolio.application.catalogo.dto.CatalogoResponse;
import br.gov.anvisa.portfolio.domain.catalogo.Catalogo;
import br.gov.anvisa.portfolio.domain.catalogo.CatalogoPort;
import br.gov.anvisa.portfolio.domain.catalogo.CatalogoTipo;
import br.gov.anvisa.portfolio.domain.shared.Pagina;
import br.gov.anvisa.portfolio.domain.shared.PaginaRequest;
import br.gov.anvisa.portfolio.domain.shared.RecursoNaoEncontradoException;
import br.gov.anvisa.portfolio.domain.shared.RegistroDuplicadoException;
import br.gov.anvisa.portfolio.domain.shared.Textos;

/**
 * Casos de uso dos catalogos de dados de referencia (tabelas TD_*).
 *
 * <p>Todos os catalogos possuem a mesma estrutura e por isso compartilham este servico,
 * discriminados por {@link CatalogoTipo}.
 */
@Service
public class CatalogoService {

    private final CatalogoPort catalogoPort;

    public CatalogoService(CatalogoPort catalogoPort) {
        this.catalogoPort = catalogoPort;
    }

    @Transactional(readOnly = true)
    public Pagina<CatalogoResponse> listar(CatalogoTipo tipo, String filtro, boolean somenteAtivos, PaginaRequest paginacao) {
        return catalogoPort.listar(tipo, filtro, somenteAtivos, paginacao)
                .map(catalogo -> paraResponse(tipo, catalogo));
    }

    /**
     * Lista integral de um catalogo, utilizada para preencher listas suspensas da interface.
     */
    @Transactional(readOnly = true)
    public List<CatalogoResponse> listarTodos(CatalogoTipo tipo, boolean somenteAtivos) {
        return catalogoPort.listarTodos(tipo, somenteAtivos)
                .stream()
                .map(catalogo -> paraResponse(tipo, catalogo))
                .toList();
    }

    @Transactional(readOnly = true)
    public CatalogoResponse buscar(CatalogoTipo tipo, Long id) {
        return paraResponse(tipo, obterObrigatorio(tipo, id));
    }

    /**
     * Resolve a descricao de um item de catalogo, devolvendo {@code null} quando nao existir.
     * Utilizado para enriquecer respostas de outros agregados sem acoplar seus servicos.
     */
    @Transactional(readOnly = true)
    public String descrever(CatalogoTipo tipo, Long id) {
        if (id == null) {
            return null;
        }
        return catalogoPort.buscarPorId(tipo, id).map(Catalogo::descricao).orElse(null);
    }

    @Transactional
    public CatalogoResponse criar(CatalogoTipo tipo, CatalogoRequest request) {
        String descricao = validarDescricao(tipo, request.descricao());
        garantirDescricaoLivre(tipo, descricao, null);

        Catalogo catalogo = new Catalogo(null,
                descricao,
                tipo.possuiVersao() ? Textos.opcional(request.versao(), 10) : null,
                tipo.possuiIndicadorLiberacao() ? request.liberado() : null,
                request.registroAtivo());

        return paraResponse(tipo, catalogoPort.inserir(tipo, catalogo));
    }

    @Transactional
    public CatalogoResponse alterar(CatalogoTipo tipo, Long id, CatalogoRequest request) {
        Catalogo existente = obterObrigatorio(tipo, id);
        String descricao = validarDescricao(tipo, request.descricao());
        garantirDescricaoLivre(tipo, descricao, id);

        Catalogo catalogo = new Catalogo(existente.id(),
                descricao,
                tipo.possuiVersao() ? Textos.opcional(request.versao(), 10) : existente.versao(),
                tipo.possuiIndicadorLiberacao() ? request.liberado() : existente.liberado(),
                request.registroAtivo());

        return paraResponse(tipo, catalogoPort.alterar(tipo, catalogo));
    }

    @Transactional
    public void desativar(CatalogoTipo tipo, Long id) {
        obterObrigatorio(tipo, id);
        catalogoPort.desativar(tipo, id);
    }

    @Transactional
    public void excluir(CatalogoTipo tipo, Long id) {
        obterObrigatorio(tipo, id);
        catalogoPort.excluir(tipo, id);
    }

    private Catalogo obterObrigatorio(CatalogoTipo tipo, Long id) {
        return catalogoPort.buscarPorId(tipo, id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(tipo.rotulo(), id));
    }

    private String validarDescricao(CatalogoTipo tipo, String descricao) {
        return Textos.obrigatorio(descricao, "Descricao", tipo.tamanhoDescricao());
    }

    private void garantirDescricaoLivre(CatalogoTipo tipo, String descricao, Long idIgnorado) {
        catalogoPort.buscarPorDescricao(tipo, descricao)
                .filter(existente -> !Objects.equals(existente.id(), idIgnorado))
                .ifPresent(existente -> {
                    throw new RegistroDuplicadoException(
                            "Ja existe um item com a descricao %s em %s".formatted(descricao, tipo.rotulo()));
                });
    }

    private CatalogoResponse paraResponse(CatalogoTipo tipo, Catalogo catalogo) {
        return new CatalogoResponse(catalogo.id(),
                catalogo.descricao(),
                tipo.possuiVersao() ? catalogo.versao() : null,
                tipo.possuiIndicadorLiberacao() ? catalogo.liberado() : null,
                catalogo.registroAtivo());
    }
}