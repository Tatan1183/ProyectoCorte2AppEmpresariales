package com.app.veterinaria.controller;

import com.app.veterinaria.dto.LoginRequest;
import com.app.veterinaria.dto.LoginResponse;
import com.app.veterinaria.model.Veterinario;
import com.app.veterinaria.repository.VeterinarioRepository;
import com.app.veterinaria.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private VeterinarioRepository veterinarioRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Buscar el veterinario por email
            Veterinario veterinario = veterinarioRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Verificar la contraseña
            if (!passwordEncoder.matches(loginRequest.getPassword(), veterinario.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales inválidas");
            }

            // Generar token
            String token = jwtTokenUtil.generateToken(veterinario.getEmail());
            
            // Limpiar la contraseña antes de enviar la respuesta
            veterinario.setPassword(null);
            
            return ResponseEntity.ok(new LoginResponse(token, veterinario));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Credenciales inválidas");
        }
    }
}

