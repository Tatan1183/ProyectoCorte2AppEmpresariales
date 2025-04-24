package com.app.veterinaria.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    // Cargar la clave secreta y la expiración del token desde el archivo de configuración
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration; // Tiempo de expiración del token en segundos

    // Método para generar un token JWT a partir del correo del usuario
    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>(); // Crear un mapa vacío para los claims
        return createToken(claims, email); // Llamar al método para crear el token
    }

    // Crear el token JWT con los claims y el sujeto (en este caso, el correo del usuario)
    private String createToken(Map<String, Object> claims, String subject) {
        // Construir y devolver el token JWT
        return Jwts.builder()
                .setClaims(claims) // Establecer los claims
                .setSubject(subject) // Establecer el sujeto (email del usuario)
                .setIssuedAt(new Date(System.currentTimeMillis())) // Establecer la fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000)) // Establecer la fecha de expiración
                .signWith(SignatureAlgorithm.HS512, secret) // Firmar el token con el algoritmo HS512 y la clave secreta
                .compact(); // Crear y devolver el token compactado
    }

    // Validar el token comparando el email y verificando que no haya expirado
    public Boolean validateToken(String token, String email) {
        final String username = extractEmail(token); // Extraer el email del token
        return (username.equals(email) && !isTokenExpired(token)); // Validar que el email coincida y que el token no haya expirado
    }

    // Extraer el email (subject) del token
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject); // Extraer el claim "subject" que contiene el email
    }

    // Extraer la fecha de expiración del token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration); // Extraer el claim "expiration"
    }

    // Método genérico para extraer cualquier claim del token
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token); // Extraer todos los claims del token
        return claimsResolver.apply(claims); // Resolver y devolver el claim solicitado
    }

    // Extraer todos los claims del token usando la clave secreta para validarlo
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody(); // Parsear el token y obtener los claims
    }

    // Verificar si el token ha expirado
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); // Comparar la fecha de expiración con la fecha actual
    }
}

