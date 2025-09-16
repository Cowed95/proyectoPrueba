package com.trabajoproyecto.demo.repository;

// importaciones necesarias para el repositorio
import com.trabajoproyecto.demo.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Interfaz del repositorio
public interface CarRepository extends JpaRepository<Car, Long> {
    // Método para encontrar todos los carros de un usuario específico por su ID
    List<Car> findByOwnerId(Long userId);
}
