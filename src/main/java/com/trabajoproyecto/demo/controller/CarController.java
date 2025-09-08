package com.trabajoproyecto.demo.controller;

import com.trabajoproyecto.demo.entity.Car;
import com.trabajoproyecto.demo.entity.User;
import com.trabajoproyecto.demo.repository.UserRepository;
import com.trabajoproyecto.demo.service.CarService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador REST para la entidad Car
@RestController
@RequestMapping("/api/cars")
public class CarController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarService carServiceNew;

    // --- CRUD general ---

    // Listar todos los carros
    @GetMapping
    public List<Car> getAllCars() {
        return carServiceNew.getAllCars();
    }

    // Obtener un carro por ID
    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        return carServiceNew.getCarById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear un nuevo carro (requiere que venga el owner con id)
    @PostMapping
    public ResponseEntity<Car> createCar(@Valid @RequestBody Car car) {
        if (car.getOwner() != null && car.getOwner().getId() != null) {
            User user = userRepository.findById(car.getOwner().getId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + car.getOwner().getId()));
            car.setOwner(user);
        } else {
            return ResponseEntity.badRequest().body(null);
        }

        Car savedCar = carServiceNew.saveCar(car);
        return ResponseEntity.ok(savedCar);
    }

    // Actualizar un carro existente
    @PutMapping("/{id}")
    public Car updateCar(@PathVariable Long id, @Valid @RequestBody Car carDetails) {
        return carServiceNew.updateCar(id, carDetails);
    }

    // Eliminar un carro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carServiceNew.deleteCar(id);
        return ResponseEntity.noContent().build();
    }

    // --- ENDPOINTS para usuarios ---

    // Crear un carro para un usuario específico
    @PostMapping("/users/{userId}")
    public ResponseEntity<Car> createCarForUser(@PathVariable Long userId, @Valid @RequestBody Car car) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + userId));

        car.setOwner(user);
        Car savedCar = carServiceNew.saveCar(car);
        return ResponseEntity.ok(savedCar);
    }

 // Listar todos los carros de un usuario
@GetMapping("/users/{userId}")
public ResponseEntity<List<Car>> getCarsByUser(@PathVariable Long userId) {
    if (!userRepository.existsById(userId)) {
        return ResponseEntity.notFound().build();
    }

    List<Car> cars = carServiceNew.getCarsByUserId(userId);
    return ResponseEntity.ok(cars);
}



}
