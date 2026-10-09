package br.gov.anvisa.portfolio.infrastructure.persistence.fake.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FakeRotinaDto(
        Long id,
        Long sistemaId,
        String nome,
        Long tipoRotinaId,
        String descricao,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {}
