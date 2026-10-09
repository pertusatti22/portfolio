package br.gov.anvisa.portfolio.infrastructure.persistence.fake.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FakeSistemaDto(
        Long id,
        String sigla,
        String nome,
        String descricao,
        Long statusSistemaId,
        Boolean anvisa,
        Boolean integracaoContinua,
        Boolean validado,
        String numeroOcorrencia,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {}
