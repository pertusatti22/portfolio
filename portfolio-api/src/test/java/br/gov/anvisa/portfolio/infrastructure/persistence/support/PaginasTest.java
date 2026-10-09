package br.gov.anvisa.portfolio.infrastructure.persistence.support;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import br.gov.anvisa.portfolio.domain.shared.PaginaRequest;

@DisplayName("Paginas")
class PaginasTest {

    private static final Set<String> COLUNAS = Set.of("nome", "sigla");

    @Test
    @DisplayName("usa a ordenacao solicitada quando a coluna e permitida")
    void ordenacaoPermitida() {
        Pageable pageable = Paginas.para(new PaginaRequest(2, 10, "sigla", false), COLUNAS, "nome");

        assertThat(pageable.getPageNumber()).isEqualTo(2);
        assertThat(pageable.getPageSize()).isEqualTo(10);
        assertThat(pageable.getSort().getOrderFor("sigla").getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    @DisplayName("descarta colunas fora da lista branca")
    void ordenacaoInvalida() {
        Pageable pageable = Paginas.para(new PaginaRequest(0, 10, "coluna_injetada", true), COLUNAS, "nome");

        assertThat(pageable.getSort().getOrderFor("nome")).isNotNull();
        assertThat(pageable.getSort().getOrderFor("coluna_injetada")).isNull();
    }

    @Test
    @DisplayName("usa a ordenacao padrao quando nenhuma coluna e informada")
    void ordenacaoPadrao() {
        Pageable pageable = Paginas.para(new PaginaRequest(0, 10, null, true), COLUNAS, "nome");

        assertThat(pageable.getSort().getOrderFor("nome")).isNotNull();
    }
}