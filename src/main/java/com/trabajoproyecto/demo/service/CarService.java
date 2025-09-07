package com.trabajoproyecto.demo.service;

// importaciones necesarias
import com.trabajoproyecto.demo.entity.Car;
import com.trabajoproyecto.demo.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Servicio para la entidad Car, manejando la lógica de negocio
@Service
public class CarService {

    // Inyectar el repositorio de carros
    @Autowired
    private CarRepository carRepository;

    // Obtener todos los carros
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    // Obtener un carro por ID
    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    // Guardar un nuevo carro
    public Car saveCar(Car car) {
        return carRepository.save(car);
    }

    // Actualizar un carro existente
    public Car updateCar(Long id, Car carDetails) {
        return carRepository.findById(id).map(car -> {
            car.setModel(carDetails.getModel());
            car.setBrand(carDetails.getBrand());
            car.setPrice(carDetails.getPrice());
            return carRepository.save(car);
        }).orElseThrow(() -> new RuntimeException("Car not found with id " + id));
    }

    // Eliminar un carro
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }
}
