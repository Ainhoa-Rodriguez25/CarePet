package com.tfg.carepet.service;

import com.tfg.carepet.dto.AuthResponse;
import com.tfg.carepet.dto.LoginRequest;
import com.tfg.carepet.dto.RegisterRequest;
import com.tfg.carepet.model.User;
import com.tfg.carepet.repository.UserRepository;
import com.tfg.carepet.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        // 1. Validar que el email no exista
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // 2. Crear usuario
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // 3. Guardar en la base de datos
        User savedUser = userRepository.save(user);

        // 4. Generar token JWT
        String token = jwtUtil.generateToken(savedUser.getId());

        // 5. Devolver respuesta
        return new AuthResponse(
                token,
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail()
        );
    }

    public AuthResponse login(LoginRequest request) {
        // 1. Buscar usuario por email
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        // 2. Verificar contraseña
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        // 3. Generar token JWT
        String token = jwtUtil.generateToken(user.getId());

        // 4. Devolver respuesta
        return  new AuthResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
