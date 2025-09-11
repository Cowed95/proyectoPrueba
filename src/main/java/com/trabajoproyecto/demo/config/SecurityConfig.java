package com.trabajoproyecto.demo.config;

// importaciones necesarias para la configuración de seguridad
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

// Configuración de seguridad para la aplicación
@Configuration
public class SecurityConfig {

    // Definición del filtro de seguridad HTTP
    @Bean
    // Configura las reglas de seguridad HTTP para la aplicación web
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
         // Desactivamos CSRF
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
            // Permitimos todas las peticiones sin autenticación
                .anyRequest().permitAll()
            );
        return http.build();
    }
}
