package com.trabajoproyecto.demo.dto;

// DTO para respuestas de autenticación
import lombok.AllArgsConstructor;
import lombok.Getter;

// Clase que representa una respuesta de autenticación con un token JWT

@Getter @AllArgsConstructor
public class AuthResponse {
    private String token;
}
