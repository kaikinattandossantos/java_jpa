package com.example.demo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
@OpenAPIDefinition(
    info = @Info(title = "API de Produtos", version = "1.0", description = "API para gerenciamento de um cadastro de produtos."),
    security = @SecurityRequirement(name = "bearerAuth") // Aplica segurança a todos os endpoints
)
public class NeurotechApplication {
    public static void main(String[] args) {
        SpringApplication.run(NeurotechApplication.class, args);
    }
}