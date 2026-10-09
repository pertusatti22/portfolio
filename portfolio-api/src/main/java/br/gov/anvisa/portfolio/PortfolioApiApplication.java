package br.gov.anvisa.portfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada da Portfolio API.
 *
 * <p>A aplicacao expoe as quatro camadas da Clean Architecture:
 * {@code domain}, {@code application}, {@code infrastructure} e {@code api}.
 */
@SpringBootApplication
public class PortfolioApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortfolioApiApplication.class, args);
    }
}