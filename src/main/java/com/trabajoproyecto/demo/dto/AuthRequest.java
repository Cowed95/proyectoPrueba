package com.trabajoproyecto.demo.dto;

// DTO para solicitudes de autenticación
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Clase que representa una solicitud de autenticación con nombre de usuario y contraseña
@Getter @Setter @NoArgsConstructor
public class AuthRequest {
    private String username;
    private String password;
}
