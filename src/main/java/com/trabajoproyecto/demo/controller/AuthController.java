package com.trabajoproyecto.demo.controller;

// Controlador REST para manejar las solicitudes de autenticación (registro e inicio de sesión)
import com.trabajoproyecto.demo.dto.AuthRequest;
import com.trabajoproyecto.demo.dto.AuthResponse;
import com.trabajoproyecto.demo.entity.User;
import com.trabajoproyecto.demo.repository.UserRepository;
import com.trabajoproyecto.demo.service.CustomUserDetailsService;
import com.trabajoproyecto.demo.service.JwtService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("El username ya existe");
        }
        User u = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(null)
                .build();
        userRepository.save(u);
        u.setPassword(null); // no devolver password
        return ResponseEntity.ok(u);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        UserDetails ud = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(ud);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
