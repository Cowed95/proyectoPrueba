package com.trabajoproyecto.demo.config;

// Configuración de seguridad para la aplicación, definiendo políticas de autenticación y autorización
// Importaciones necesarias
import com.trabajoproyecto.demo.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Anotaciones para definir una clase de configuración en Spring

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    // Filtro personalizado para manejar la autenticación basada en JWT
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Bean para el codificador de contraseñas utilizando BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuración de la cadena de filtros de seguridad
    @Bean

    // Definición de las políticas de seguridad HTTP, incluyendo rutas permitidas y autenticación JWT
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                // Permitir acceso a Swagger UI y documentación de la API sin autenticación
                .requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**",
                    "/v3/api-docs"
                ).permitAll()
                // Cualquier otra solicitud requiere autenticación
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            // Configuración adicional para permitir el uso de la consola H2
            http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));
        return http.build();
    }

    // Bean para el gestor de autenticación, necesario para la autenticación personalizada
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
