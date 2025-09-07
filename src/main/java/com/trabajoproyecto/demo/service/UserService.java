package com.trabajoproyecto.demo.service;

import com.trabajoproyecto.demo.entity.User;
import com.trabajoproyecto.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Guardar usuario
    public User saveUser(User user) {
        return userRepository.save(user);
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
