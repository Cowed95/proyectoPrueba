package com.trabajoproyecto.demo.config;

// Configuración de Swagger/OpenAPI para la documentación de la API
// Importaciones necesarias
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Anotación para definir una clase de configuración en Spring
@Configuration
public class SwaggerConfig {

    // Definición del bean OpenAPI con la información del proyecto y enlaces externos
    @Bean

    // Configuración de la documentación OpenAPI para la API del proyecto
    public OpenAPI projectOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("ProyectoPrueba API")
                        .description("Documentación de la API para ProyectoPrueba")
                        .version("v1.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("GitHub Repository")
                        .url("https://github.com/Cowed95/proyectoPrueba"));
    }
}
