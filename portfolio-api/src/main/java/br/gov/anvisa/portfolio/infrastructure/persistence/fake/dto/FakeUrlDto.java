package br.gov.anvisa.portfolio.infrastructure.persistence.fake.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FakeUrlDto(
        Long id,
        Long sistemaId,
        Long tipoUrlId,
        String url,
        String ambiente,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {}
