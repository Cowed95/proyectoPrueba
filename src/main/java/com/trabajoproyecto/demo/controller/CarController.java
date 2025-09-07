package com.trabajoproyecto.demo.controller;

// importaciones necesarias
import com.trabajoproyecto.demo.entity.Car;
import com.trabajoproyecto.demo.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador REST para la entidad Car, manejando las solicitudes HTTP
@RestController
@RequestMapping("/cars")

// Anotación para inyección de dependencias
public class CarController {

    // Inyectar el servicio de carros
    @Autowired
    private CarService carService;

    // Definir los endpoints REST para manejar las operaciones CRUD
        // Listar todos los carros
    @GetMapping
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

        // Obtener un carro por ID
    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        return carService.getCarById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

        // Crear un nuevo carro
    @PostMapping
    public Car createCar(@RequestBody Car car) {
        return carService.saveCar(car);
    }

        // Actualizar un carro existente
    @PutMapping("/{id}")
    public Car updateCar(@PathVariable Long id, @RequestBody Car carDetails) {
        return carService.updateCar(id, carDetails);
    }

        // Eliminar un carro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }
}
