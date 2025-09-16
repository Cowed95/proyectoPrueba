package com.trabajoproyecto.demo.service;

// Servicio para manejar la generación y validación de tokens JWT
// importaciones necesarias para trabajar con JWT
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.security.core.userdetails.UserDetails;

// Clase que proporciona métodos para generar y validar tokens JWT

@Service
public class JwtService {

    // Clave secreta y tiempo de expiración del token, inyectados desde application.properties
    @Value("${jwt.secret}")
    private String secret;

    // Tiempo de expiración en milisegundos
    @Value("${jwt.expirationMs}")
    private long expirationMs;

    // Método para obtener la clave de firma a partir de la clave secreta
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // Genera un token JWT para el usuario dado

    public String generateToken(UserDetails userDetails) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extrae el nombre de usuario del token JWT

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                // Verifica la firma del token y extrae el sujeto (nombre de usuario)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Valida el token JWT para el usuario dado

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return username != null && username.equals(userDetails.getUsername()) &&
                    !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    // Verifica si el token ha expirado

    private boolean isTokenExpired(String token) {
        Date exp = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return exp.before(new Date());
    }
}
