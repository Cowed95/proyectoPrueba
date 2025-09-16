package com.trabajoproyecto.demo.service;

// importaciones necesarias
import com.trabajoproyecto.demo.entity.Car;
import com.trabajoproyecto.demo.entity.User;
import com.trabajoproyecto.demo.repository.CarRepository;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Servicio para la entidad Car, manejando la lógica de negocio
@Getter
@Setter
@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    // -----
    // Métodos generales
    // -----

    // Guardar carro sin validar usuario
    public Car saveCar(Car car) {
        return carRepository.save(car);
    }

    // Obtener todos los carros
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    // Obtener carro por id
    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    // Actualizar carro sin validar usuario
    public Car updateCar(Long id, Car carDetails) {
        return carRepository.findById(id).map(car -> {
            car.setBrand(carDetails.getBrand());
            car.setModel(carDetails.getModel());
            car.setYear(carDetails.getYear());
            car.setPlate(carDetails.getPlate());
            car.setColor(carDetails.getColor());
            car.setPrice(carDetails.getPrice());
            return carRepository.save(car);
        }).orElseThrow(() -> new RuntimeException("Vehiculo no encontrado con id " + id));
    }

    // Eliminar carro sin validar usuario
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    // Métodos con validación de usuario

    // Obtener todos los carros de un usuario específico
    public List<Car> getCarsByUser(User owner) {
        return carRepository.findByOwnerId(owner.getId());
    }

    // Obtener todos los carros de un usuario por su ID
    public List<Car> getCarsByUserId(Long userId) {
        return carRepository.findByOwnerId(userId);
    }

    // Obtener un carro por ID, validando que sea del usuario
    public Optional<Car> getCarByIdForUser(Long id, User owner) {
        return carRepository.findById(id)
                .filter(car -> car.getOwner().getId().equals(owner.getId()));
    }

    // Guardar un nuevo carro para un usuario específico
    public Car saveCarForUser(Car car, User owner) {
        car.setOwner(owner); // Asignar el dueño
        return carRepository.save(car);
    }

    // Actualizar un carro existente solo si pertenece al usuario
    public Car updateCarForUser(Long id, Car carDetails, User owner) {
        return carRepository.findById(id)
                .filter(car -> car.getOwner().getId().equals(owner.getId()))
                .map(car -> {
                    car.setBrand(carDetails.getBrand());
                    car.setModel(carDetails.getModel());
                    car.setYear(carDetails.getYear());
                    car.setPlate(carDetails.getPlate());
                    car.setColor(carDetails.getColor());
                    car.setPrice(carDetails.getPrice());
                    return carRepository.save(car);
                })
                .orElseThrow(() -> new RuntimeException("Vehiculo no encontrado o no pertenece al usuario"));
    }

    // Eliminar un carro solo si pertenece al usuario
    public void deleteCarForUser(Long id, User owner) {
        Car car = carRepository.findById(id)
                .filter(c -> c.getOwner().getId().equals(owner.getId()))
                .orElseThrow(() -> new RuntimeException("Vehiculo no encontrado o no pertenece al usuario"));
        carRepository.delete(car);
    }
}
