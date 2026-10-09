package br.gov.anvisa.portfolio.config;

import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Beans transversais da aplicacao.
 */
@Configuration
public class AppConfig {

    /**
     * Relogio injetavel, utilizado para calculos de vigencia e testes deterministicos.
     */
    @Bean
    Clock clock() {
        return Clock.systemDefaultZone();
    }
}