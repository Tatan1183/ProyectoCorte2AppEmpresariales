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

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/auth") // Define la ruta base para las solicitudes de autenticación
@CrossOrigin(origins = "*") // Permite solicitudes CORS desde cualquier origen (útil en aplicaciones front-end separadas)
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager; // Maneja la autenticación de usuarios

    @Autowired
    private VeterinarioRepository veterinarioRepository; // Repositorio para acceder a los datos de los veterinarios

    @Autowired
    private JwtTokenUtil jwtTokenUtil; // Utilidad para generar y verificar tokens JWT

    @Autowired
    private PasswordEncoder passwordEncoder; // Codificador de contraseñas para verificar las contraseñas de manera segura

    // Método para manejar el inicio de sesión
    @PostMapping("/login") // Indica que este método responde a solicitudes POST en la ruta "/login"
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Buscar el veterinario en la base de datos por su email
            Veterinario veterinario = veterinarioRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Verificar que la contraseña proporcionada coincida con la almacenada en la base de datos
            if (!passwordEncoder.matches(loginRequest.getPassword(), veterinario.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED) // Devuelve un error de no autorizado si las credenciales son incorrectas
                        .body("Credenciales inválidas");
            }

            // Si las credenciales son correctas, generar un token JWT para el usuario
            String token = jwtTokenUtil.generateToken(veterinario.getEmail());

            // Limpiar la contraseña antes de devolver la información del veterinario en la respuesta
            veterinario.setPassword(null);

            // Retornar la respuesta con el token y los detalles del veterinario
            return ResponseEntity.ok(new LoginResponse(token, veterinario));
        } catch (Exception e) {
            // En caso de error, devolver una respuesta de error 401 con un mensaje de credenciales inválidas
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales inválidas");
        }
    }
}
