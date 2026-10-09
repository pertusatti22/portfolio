package br.gov.anvisa.portfolio.infrastructure.persistence.fake.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FakeServidorDto(
        Long id,
        String nome,
        String enderecoIp,
        String descricao,
        Boolean ligado,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {}
