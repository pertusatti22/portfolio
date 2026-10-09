package br.gov.anvisa.portfolio.infrastructure.persistence.fake.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FakeVinculoDto(
        Long id,
        Long sistemaId,
        String tipoVinculo,
        Long referenciaId,
        Boolean principal
) {}
