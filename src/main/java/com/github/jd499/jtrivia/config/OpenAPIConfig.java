package com.github.jd499.jtrivia.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.ExternalDocumentation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI usersMicroserviceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("JTrivia API")
                        .description("A Jeopardy! API that provides access to questions, categories, and games from every main season.")
                        .version("1.0")
                        .contact(new Contact()
                                .name("GitHub")
                                .url("https://github.com/jd499")))
                .externalDocs(new ExternalDocumentation()
                        .description("Source Code")
                        .url("https://github.com/jd499/jtrivia"));
    }
}