package br.gov.anvisa.portfolio.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI portfolioOpenApi(@Value("${portfolio.openapi.title}") String title,
                             @Value("${portfolio.openapi.description}") String description,
                             @Value("${portfolio.openapi.version}") String version) {
        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .description(description)
                        .version(version)
                        .license(new License().name("Proprietary")))
                .servers(java.util.Collections.singletonList(new Server().url("/").description("Servidor corrente")))
                .components(new Components());
    }
}