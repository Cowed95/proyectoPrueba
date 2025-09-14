package com.trabajoproyecto.demo.config;

// Configuración de seguridad para la aplicación Spring Boot
// Importaciones necesarias
import com.trabajoproyecto.demo.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Anotaciones para definir una clase de configuración en Spring y para inyección de dependencias
@Configuration
@RequiredArgsConstructor
// Clase de configuración de seguridad que define cómo se manejan las solicitudes HTTP y la autenticación
public class SecurityConfig {

    // Filtro de autenticación JWT para validar tokens en las solicitudes
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Bean para codificar contraseñas usando BCrypt algoritmo
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuración de la cadena de filtros de seguridad para manejar las solicitudes HTTP
    // Define qué rutas son públicas y cuáles requieren autenticación y agrega el filtro JWT
    // antes del filtro de autenticación de nombre de usuario y contraseña de Spring Security
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",
                                "/h2-console/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Bean para gestionar la autenticación usando la configuración de autenticación proporcionada por Spring Security
    // Permite inyectar el AuthenticationManager en otros componentes si es necesario
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // Configuración para ignorar ciertas rutas de la seguridad web, como las relacionadas con Swagger y H2 Console
    // Esto permite el acceso sin autenticación a estas rutas
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/swagger-resources/**",
                "/webjars/**",
                "/h2-console/**");
    }

}
