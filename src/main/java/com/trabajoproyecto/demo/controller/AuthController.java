package com.trabajoproyecto.demo.controller;

// Controlador REST para manejar las solicitudes de autenticación (registro e inicio de sesión)
// Importaciones necesarias
import com.trabajoproyecto.demo.dto.AuthRequest;
import com.trabajoproyecto.demo.dto.AuthResponse;
import com.trabajoproyecto.demo.entity.User;
import com.trabajoproyecto.demo.repository.UserRepository;
import com.trabajoproyecto.demo.service.JwtService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

// Anotaciones para definir un controlador REST y mapear las solicitudes a /api/auth
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // Endpoint para manejar el registro de nuevos usuarios y el almacenamiento seguro de sus credenciales
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("El username ya existe");
        }
        User u = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role("ROLE_USER")
                .build();
        userRepository.save(u);
        u.setPassword(null); // Ocultar la contraseña en la respuesta
        return ResponseEntity.ok(u);
    }

    // Endpoint para manejar el inicio de sesión y la generación del token JWT
    @PostMapping("/login")
public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
    Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
    );
    // Obtener los detalles del usuario autenticado
    UserDetails ud = (UserDetails) auth.getPrincipal();
    String token = jwtService.generateToken(ud);
    return ResponseEntity.ok(new AuthResponse(token));
}

}
