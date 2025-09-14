package com.trabajoproyecto.demo.security;

// Filtro de autenticación JWT que se ejecuta una vez por solicitud HTTP y verifica el token JWT en el encabezado Authorization
// si el token es válido, establece la autenticación en el contexto de seguridad de Spring
// y si no, permite que la solicitud continúe sin autenticación

// importaciones necesarias para el filtro
import com.trabajoproyecto.demo.service.JwtService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.trabajoproyecto.demo.service.CustomUserDetailsService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

// Componente de Spring que actúa como un filtro para autenticar solicitudes usando JWT
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Servicio para manejar operaciones relacionadas con JWT
    // Servicio para cargar detalles del usuario

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    // Constructor para inyectar las dependencias necesarias

    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService uds) {
        this.jwtService = jwtService;
        this.userDetailsService = uds;
    }

    // Método que se ejecuta para cada solicitud HTTP para autenticar al usuario
    // basado en el token JWT
    // si está presente en el encabezado Authorization de la solicitud

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // Ignorar rutas públicas (Swagger, H2, Auth)
        String path = request.getServletPath();
        if (path.startsWith("/api/auth")
                || path.startsWith("/swagger-ui")
                || path.equals("/swagger-ui.html")
                || path.equals("/v3/api-docs")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-resources")
                || path.startsWith("/webjars")
                || path.startsWith("/h2-console")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;

        // Extrae el token JWT del encabezado Authorization si está presente y comienza
        // con "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = jwtService.extractUsername(token);
            } catch (Exception ignored) {
            }
        }

        // Si se extrajo un nombre de usuario y no hay una autenticación ya establecida
        // en el contexto de seguridad
        // carga los detalles del usuario y valida el token. Si es válido, establece la
        // autenticación en el contexto de seguridad
        // y si no, continúa con el filtro sin autenticar
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails ud = userDetailsService.loadUserByUsername(username);
            if (jwtService.validateToken(token, ud)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(ud, null,
                        ud.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
