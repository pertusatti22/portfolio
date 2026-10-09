package br.gov.anvisa.portfolio.infrastructure.persistence.support;

import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import br.gov.anvisa.portfolio.domain.shared.PaginaRequest;

/**
 * Conversao da paginacao de dominio para {@link Pageable}, com lista branca de colunas.
 */
public final class Paginas {

    private Paginas() {
    }

    public static Pageable para(PaginaRequest requisicao, Set<String> colunasValidas, String ordenacaoPadrao) {
        String ordenacao = requisicao.ordenacao() != null && colunasValidas.contains(requisicao.ordenacao())
                ? requisicao.ordenacao()
                : ordenacaoPadrao;
        Sort.Direction direcao = requisicao.crescente() ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(requisicao.pagina(), requisicao.tamanho(), Sort.by(direcao, ordenacao));
    }
}