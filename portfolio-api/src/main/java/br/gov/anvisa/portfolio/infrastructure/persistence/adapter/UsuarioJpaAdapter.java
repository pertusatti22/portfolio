package br.gov.anvisa.portfolio.infrastructure.persistence.adapter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.anvisa.portfolio.domain.shared.Pagina;
import br.gov.anvisa.portfolio.domain.usuario.TipoOperacao;
import br.gov.anvisa.portfolio.domain.usuario.Usuario;
import br.gov.anvisa.portfolio.domain.usuario.UsuarioFiltro;
import br.gov.anvisa.portfolio.domain.usuario.UsuarioHistorico;
import br.gov.anvisa.portfolio.domain.usuario.UsuarioPort;
import br.gov.anvisa.portfolio.domain.usuario.UsuarioHistoricoFiltro;
import br.gov.anvisa.portfolio.infrastructure.persistence.entity.UsuarioEntity;
import br.gov.anvisa.portfolio.infrastructure.persistence.repository.UsuarioJpaRepository;
import br.gov.anvisa.portfolio.infrastructure.persistence.support.Paginas;
import jakarta.persistence.criteria.Predicate;

/**
 * Adaptador da porta {@link UsuarioPort}.
 *
 * <p>TB_USUARIO e acessada por JPA; o historico TB_HIST_USUARIO, que nao possui chave
 * primaria e e alimentado por trigger, e consultado por JDBC.
 */
@Repository
public class UsuarioJpaAdapter implements UsuarioPort {

    private static final Set<String> COLUNAS_ORDENACAO = Set.of("username", "host", "tipoUsuarioId", "inicioVigencia");

    private final UsuarioJpaRepository repositorio;
    private final JdbcTemplate jdbcTemplate;
    private final String esquema;

    public UsuarioJpaAdapter(UsuarioJpaRepository repositorio,
                             JdbcTemplate jdbcTemplate,
                             @Value("${portfolio.jdbc.esquema:DBPORTIFOLIO}") String esquema) {
        this.repositorio = repositorio;
        this.jdbcTemplate = jdbcTemplate;
        this.esquema = esquema;
    }

    @Override
    @Transactional(readOnly = true)
    public Pagina<Usuario> listar(UsuarioFiltro filtro) {
        Pageable pageable = Paginas.para(filtro.paginacao(), COLUNAS_ORDENACAO, "username");
        Page<UsuarioEntity> pagina = repositorio.findAll(especificacao(filtro), pageable);
        List<Usuario> itens = pagina.getContent().stream().map(this::paraDominio).toList();
        return Pagina.de(itens, pagina.getNumber(), pagina.getSize(), pagina.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorUsername(String username) {
        return repositorio.findById(username).map(this::paraDominio);
    }

    @Override
    @Transactional
    public Usuario inserir(Usuario usuario) {
        UsuarioEntity entity = new UsuarioEntity();
        aplicar(entity, usuario);
        return paraDominio(repositorio.save(entity));
    }

    @Override
    @Transactional
    public Usuario alterar(Usuario usuario) {
        UsuarioEntity entity = repositorio.findById(usuario.username()).orElseThrow();
        aplicar(entity, usuario);
        return paraDominio(repositorio.save(entity));
    }

    @Override
    @Transactional
    public void excluir(String username) {
        repositorio.deleteById(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Pagina<UsuarioHistorico> listarHistorico(UsuarioHistoricoFiltro filtro) {
        List<Object> parametros = new ArrayList<>();
        StringBuilder where = new StringBuilder();

        if (filtro.username() != null) {
            where.append(where.isEmpty() ? " WHERE " : " AND ").append("UPPER(CO_USERNAME) = UPPER(?)");
            parametros.add(filtro.username());
        }
        if (filtro.tipoUsuarioId() != null) {
            where.append(where.isEmpty() ? " WHERE " : " AND ").append("CO_TIPO_USUARIO = ?");
            parametros.add(filtro.tipoUsuarioId());
        }
        if (filtro.operacao() != null) {
            where.append(where.isEmpty() ? " WHERE " : " AND ").append("UPPER(TP_OPERACAO) = ?");
            parametros.add(String.valueOf(filtro.operacao().codigo()));
        }

        String tabela = "%s.TB_HIST_USUARIO".formatted(esquema);
        Long total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM %s%s".formatted(tabela, where),
                Long.class, parametros.toArray());
        long totalElementos = total == null ? 0L : total;

        var requisicao = filtro.paginacao();
        int deslocamento = requisicao.pagina() * requisicao.tamanho();
        if (deslocamento >= totalElementos) {
            return Pagina.de(List.of(), requisicao.pagina(), requisicao.tamanho(), totalElementos);
        }

        String sql = ("SELECT CO_USERNAME, DS_HOST, CO_TIPO_USUARIO, DT_INICIO_VIGENCIA, DT_FIM_VIGENCIA, "
                + "CO_USUARIO_BANCO_DADOS, DS_USUARIO_APLICACAO, DT_REGISTRO, TP_OPERACAO FROM %s%s"
                + " ORDER BY DT_REGISTRO DESC OFFSET %d ROWS FETCH NEXT %d ROWS ONLY")
                .formatted(tabela, where, deslocamento, requisicao.tamanho());
        List<UsuarioHistorico> itens = jdbcTemplate.query(sql, mapeadorHistorico(), parametros.toArray());
        return Pagina.de(itens, requisicao.pagina(), requisicao.tamanho(), totalElementos);
    }

    private Specification<UsuarioEntity> especificacao(UsuarioFiltro filtro) {
        return (raiz, consulta, criteriaBuilder) -> {
            List<Predicate> condicoes = new ArrayList<>();

            if (filtro.termo() != null) {
                String busca = "%" + filtro.termo().toUpperCase() + "%";
                condicoes.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.upper(raiz.get("username")), busca),
                        criteriaBuilder.like(criteriaBuilder.upper(raiz.get("host")), busca)));
            }
            if (filtro.tipoUsuarioId() != null) {
                condicoes.add(criteriaBuilder.equal(raiz.get("tipoUsuarioId"), filtro.tipoUsuarioId()));
            }
            if (filtro.usuarioBancoDadosId() != null) {
                condicoes.add(criteriaBuilder.equal(raiz.get("usuarioBancoDadosId"), filtro.usuarioBancoDadosId()));
            }
            if (Boolean.TRUE.equals(filtro.somenteVigentes())) {
                condicoes.add(criteriaBuilder.or(
                        criteriaBuilder.isNull(raiz.get("fimVigencia")),
                        criteriaBuilder.greaterThanOrEqualTo(raiz.get("fimVigencia"), LocalDate.now())));
            }
            return condicoes.isEmpty() ? criteriaBuilder.conjunction()
                    : criteriaBuilder.and(condicoes.toArray(Predicate[]::new));
        };
    }

    private void aplicar(UsuarioEntity entity, Usuario usuario) {
        entity.setHost(usuario.host());
        entity.setTipoUsuarioId(usuario.tipoUsuarioId());
        entity.setInicioVigencia(usuario.inicioVigencia());
        entity.setFimVigencia(usuario.fimVigencia());
        entity.setUsuarioBancoDadosId(usuario.usuarioBancoDadosId());
        entity.setUsuarioSecundario(usuario.usuarioSecundario());
    }

    private Usuario paraDominio(UsuarioEntity entity) {
        return new Usuario(entity.getUsername(),
                entity.getHost(),
                entity.getTipoUsuarioId(),
                entity.getInicioVigencia(),
                entity.getFimVigencia(),
                entity.getUsuarioBancoDadosId(),
                entity.getUsuarioSecundario());
    }

    private RowMapper<UsuarioHistorico> mapeadorHistorico() {
        return (resultado, indice) -> {
            Timestamp registro = resultado.getTimestamp("DT_REGISTRO");
            return new UsuarioHistorico(resultado.getString("CO_USERNAME"),
                    resultado.getString("DS_HOST"),
                    resultado.getLong("CO_TIPO_USUARIO"),
                    paraData(resultado.getTimestamp("DT_INICIO_VIGENCIA")),
                    paraData(resultado.getTimestamp("DT_FIM_VIGENCIA")),
                    resultado.getLong("CO_USUARIO_BANCO_DADOS"),
                    resultado.getString("DS_USUARIO_APLICACAO"),
                    registro == null ? null : registro.toInstant(),
                    TipoOperacao.doCodigo(Objects.requireNonNull(resultado.getString("TP_OPERACAO")).charAt(0)));
        };
    }

    private LocalDate paraData(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime().toLocalDate();
    }
}
