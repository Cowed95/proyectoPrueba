package com.trabajoproyecto.demo.entity;

// importaciones necesarias
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

// Anotaciones de Lombok para generar getters, setters, constructores y el patrón builder
@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    
    // Identificador único del usuario, generado automáticamente
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // El nombre de usuario debe ser único y no nulo
    @Column(nullable = false, unique = true)
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 4, max = 30, message = "El usuario debe tener entre 4 y 30 caracteres")
    private String username;

    // El email debe ser válido y no nulo 
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener un formato valido (ejemplo@dominio.com)")
    private String email;

    // La contraseña no debe ser nula ni vacía y debe tener al menos 4 caracteres
    @Column(nullable = false)
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 4, message = "La contraseña debe tener al menos 4 caracteres")
    private String password;

    // El rol del usuario, por ejemplo, "USER" o "ADMIN"
    @Column(nullable = false)
    private String role;
}
