package br.gov.anvisa.portfolio.infrastructure.persistence.fake.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FakeCatalogoDto(
        String tipo,
        String codigo,
        String descricao,
        String sigla,
        Boolean liberado,
        Integer ordem,
        Boolean ativo
) {}
