package com.trabajoproyecto.demo.dto;

// importaciones de Lombok para generar código boilerplate
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Clase que representa una solicitud de autenticación con nombre de usuario y contraseña
@Getter @Setter @NoArgsConstructor
// Anotaciones de Lombok para generar getters, setters y un constructor sin argumentos
public class AuthRequest {
    private String username;
    private String password;
    private String email;
}
