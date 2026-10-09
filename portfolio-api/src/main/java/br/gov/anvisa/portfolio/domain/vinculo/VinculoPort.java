package br.gov.anvisa.portfolio.domain.vinculo;

import java.util.Collection;
import java.util.List;

/**
 * Porta de saida para as tabelas de relacionamento (RL_*).
 *
 * <p>{@code donoId} referencia o agregadoproprietario (sistema ou servidor) e
 * {@code vinculadoId} referencia a entidade relacionada (schema, url, software, servidor,
 * base de dados ou backup).
 */
public interface VinculoPort {

    List<Long> listarVinculados(Long donoId, TipoVinculo tipo);

    /**
     * Lista os proprietarios (sistemas ou servidores) vinculados a uma entidade.
     *
     * @param vinculadoId identificador da entidade vinculada (ex.: backup)
     */
    List<Long> listarDonos(Long vinculadoId, TipoVinculo tipo);

    /**
     * @return identificadores vinculados marcados como principal (aplicavel a {@link TipoVinculo#SCHEMA})
     */
    List<Long> listarPrincipais(Long donoId, TipoVinculo tipo);

    boolean possuiVinculo(Long donoId, TipoVinculo tipo, Long vinculadoId);

    void vincular(Long donoId, TipoVinculo tipo, Collection<Long> vinculadosIds);

    void desvincular(Long donoId, TipoVinculo tipo, Collection<Long> vinculadosIds);

    /**
     * Substitui integralmente o conjunto de vinculos do dono.
     */
    void substituirVinculos(Long donoId, TipoVinculo tipo, Collection<Long> vinculadosIds);

    void definirPrincipal(Long donoId, TipoVinculo tipo, Long vinculadoId);

    void removerTodos(Long donoId, TipoVinculo tipo);

    /**
     * Remove todos os vinculos que apontam para uma entidade (ex.: exclusao de um backup).
     */
    void desvincularDonos(Long vinculadoId, TipoVinculo tipo);
}