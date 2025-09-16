package com.trabajoproyecto.demo.entity;

// importaciones necesarias
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

// Anotaciones de Lombok para generar getters, setters, constructores y el patrón builder
@Entity
@Table(name = "cars")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Car {

    // Identificador único del carro
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Marca obligatoria
    @NotBlank(message = "La marca es obligatoria")
    private String brand;

    // Modelo obligatorio
    @NotBlank(message = "El modelo es obligatorio")
    private String model;

    // Año obligatorio
    @NotNull(message = "El año es obligatorio")
    private Integer year;

    // Placa obligatoria y única
    @Column(nullable = false, unique = true)
    @NotBlank(message = "La placa es obligatoria")
    private String plate;

    // Color obligatorio
    @NotBlank(message = "El color es obligatorio")
    private String color;

    // Precio obligatorio y positivo
    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private Double price;

    // Relación Many-to-One con el propietario
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;
}
