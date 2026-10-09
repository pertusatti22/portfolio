package br.gov.anvisa.portfolio.infrastructure.persistence.fake.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FakeBackupDto(
        Long id,
        Long sistemaId,
        Long tipoBackupId,
        String caminho,
        String frequencia,
        Boolean eliminaArchiveLog,
        Integer retencao,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {}
