package br.gov.anvisa.portfolio.domain.documentacao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.gov.anvisa.portfolio.domain.shared.RegraDeNegocioException;

@DisplayName("Documentacao")
class DocumentacaoTest {

    @Test
    @DisplayName("cria documentacao ativa")
    void criacao() {
        Documentacao documentacao = Documentacao.criar(1L, "https://docs.example.gov.br", 2L);

        assertThat(documentacao.urlRepositorio()).isEqualTo("https://docs.example.gov.br");
        assertThat(documentacao.tipoDocumentacaoId()).isEqualTo(2L);
        assertThat(documentacao.registroAtivo()).isTrue();
    }

    @Test
    @DisplayName("recusa URL sem protocolo http")
    void recusaProtocolo() {
        assertThatThrownBy(() -> Documentacao.criar(1L, "docs.example.gov.br", 2L))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("http");
    }

    @Test
    @DisplayName("exige tipo de documentacao")
    void tipoObrigatorio() {
        assertThatThrownBy(() -> Documentacao.criar(1L, "https://docs.example.gov.br", null))
                .isInstanceOf(RegraDeNegocioException.class);
    }
}