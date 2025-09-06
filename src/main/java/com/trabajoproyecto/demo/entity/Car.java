package com.trabajoproyecto.demo.entity;

import jakarta.persistence.*;
import lombok.*;

// Anotaciones de Lombok para generar getters, setters, constructores y el patrón builder automáticamente
@Entity
@Table(name = "cars")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder

public class Car {
   
    @Id

    //Generamos el ID de forma automatica y incrementamos en 1 cada vez que se cree un nuevo carro
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Atributos del carro
    private String brand;
    private String model;
    private int year;
    private String plate;
    private String color;

    // Relación Many-to-One con la entidad User (propietario del carro)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;
}
