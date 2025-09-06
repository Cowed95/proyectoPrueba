package com.trabajoproyecto.demo.entity;

import jakarta.persistence.*;
import lombok.*;

// Anotaciones de Lombok para generar getters, setters, constructores y el patrón builder
@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder

public class User {
    
    @Id
    //Generamos el ID de forma automatica y incrementamos en 1 cada vez que se cree un nuevo usuario
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // El nombre de usuario debe ser unico y no nulo
    @Column(nullable = false, unique = true)
    private String username;

    private String email;

    // La contraseña no debe ser nula
    @Column(nullable = false)
    private String password;
}
