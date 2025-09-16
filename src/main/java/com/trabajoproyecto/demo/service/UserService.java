package com.trabajoproyecto.demo.service;

// importaciones necesarias
import com.trabajoproyecto.demo.entity.User;
import com.trabajoproyecto.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Servicio para la entidad User, manejando la lógica de negocio
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Guardar usuario
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Actualizar usuario
    public User updateUser(Long id, User userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(userDetails.getUsername());
                    user.setEmail(userDetails.getEmail());
                    user.setPassword(userDetails.getPassword());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id " + id));
    }

    // Listar todos los usuarios
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Buscar usuario por id
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Eliminar usuario
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
