package com.back.eventscollector.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger configuration
 *
 * Swagger-UI: http://[service-host]:[service-port]/swagger-ui.html
 * */

@Configuration
public class OpenApiConfig {

    private static final String TITLE = "Events Collector";
    private static final String DESCRIPTION = "Simple events collector service.";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().components(new Components())
                .info(new Info().title(TITLE).description(DESCRIPTION));

    }
}
