package com.trabajoproyecto.demo.service;

// Servicio para cargar detalles del usuario desde la base de datos para autenticación
// importaciones necesarias para el servicio
import com.trabajoproyecto.demo.entity.User;
import com.trabajoproyecto.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

// Implementación de UserDetailsService para cargar usuarios por nombre de usuario
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    // Repositorio para acceder a los datos de usuario
    private final UserRepository userRepository;

    // Cargar usuario por nombre de usuario para autenticación
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return new org.springframework.security.core.userdetails.User(
                u.getUsername(),
                u.getPassword(),
                Collections.emptyList()
        );
    }
}
