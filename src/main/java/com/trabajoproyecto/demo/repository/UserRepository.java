package com.trabajoproyecto.demo.repository;

// Repositorio para la entidad User, extendiendo JpaRepository para operaciones CRUD
import com.trabajoproyecto.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// Interfaz del repositorio
public interface UserRepository extends JpaRepository<User, Long> {

    // Método para encontrar un usuario por su nombre de usuario
    Optional<User> findByUsername(String username);
}
