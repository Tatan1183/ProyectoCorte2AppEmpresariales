package com.app.veterinaria.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Indica que esta clase es una clase de configuración de Spring
@EnableWebSecurity // Habilita la seguridad web en la aplicación
public class SecurityConfig {

    // Inyección de dependencias para el filtro de JWT
    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    // Configurar el filtro de seguridad HTTP
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() // Desactivar la protección CSRF (ya que se usa JWT, no se necesita)
                .authorizeHttpRequests() // Configurar las autorizaciones para las solicitudes HTTP
                .requestMatchers( // Rutas públicas que no requieren autenticación
                        "/api/auth/**",
                        "/login.html",
                        "/index.html",
                        "/",
                        "/favicon.ico",
                        "/style.css",
                        "/app.js",
                        "/auth.js",
                        "/logo3.jpg",
                        "/images/**"
                ).permitAll() // Permitir acceso sin autenticación a estas rutas
                .anyRequest().authenticated() // Todas las demás rutas requieren autenticación
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // Configurar la política de creación de sesión como STATELESS (sin sesión del servidor)

        // Añadir el filtro JWT antes del filtro de autenticación estándar de Spring
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // Devolver la configuración del filtro de seguridad
        return http.build();
    }

    // Crear un bean para el AuthenticationManager, necesario para la autenticación basada en Spring Security
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager(); // Retorna el AuthenticationManager configurado por Spring
    }

    // Crear un bean para el PasswordEncoder, utilizado para cifrar las contraseñas con BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Usar BCrypt como el algoritmo para encriptar las contraseñas
    }
}

