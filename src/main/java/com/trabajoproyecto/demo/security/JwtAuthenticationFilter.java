package com.trabajoproyecto.demo.security;

// Filtro de autenticación JWT que se ejecuta una vez por solicitud
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

    // Método que se ejecuta para cada solicitud HTTP para autenticar al usuario basado en el token JWT

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = jwtService.extractUsername(token);
            } catch (Exception ignored) {}
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails ud = userDetailsService.loadUserByUsername(username);
            if (jwtService.validateToken(token, ud)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
