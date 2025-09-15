package com.trabajoproyecto.demo.config;

// Configuración de OpenAPI para documentar la API con soporte para JWT
// importaciones necesarias para OpenAPI
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

// Clase de configuración para OpenAPI con soporte para JWT en los endpoints protegidos
@Configuration
public class OpenApiConfig {

    // Define el esquema de seguridad para JWT y lo aplica globalmente a la API
    @Bean
    @Primary
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Proyecto")
                        .version("1.0")
                        .description("Documentación de la API con JWT"))
                .components(new Components()
                // Define un esquema de seguridad HTTP Bearer para JWT
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        // Indica que el formato del token es JWT
                                        .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("bearer-key"));
                // Aplica el esquema de seguridad a todos los endpoints de la API 
    }
}
