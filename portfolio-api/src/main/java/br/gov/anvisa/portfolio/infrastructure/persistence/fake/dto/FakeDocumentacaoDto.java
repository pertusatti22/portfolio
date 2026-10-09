package br.gov.anvisa.portfolio.infrastructure.persistence.fake.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FakeDocumentacaoDto(
        Long id,
        Long sistemaId,
        String nome,
        String link,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {}
