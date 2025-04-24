package com.app.veterinaria.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger; // Importar Logger para logging
import org.slf4j.LoggerFactory; // Importar LoggerFactory para crear instancias de Logger
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils; // Helper para verificar si las strings no son nulas o vacías
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    // Logger para registrar información en los logs
    private static final Logger log = LoggerFactory.getLogger(JwtTokenFilter.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil; // Utilidad para manejar el JWT

    @Autowired
    private CustomUserDetailsService userDetailsService; // Servicio personalizado para cargar los detalles del usuario

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Obtener el valor del header 'Authorization' de la solicitud HTTP
        final String requestTokenHeader = request.getHeader("Authorization");
        String requestURI = request.getRequestURI(); // Obtener la URI para registrar información

        // Registrar la URI de la solicitud en los logs
        log.debug("Processing request to: {}", requestURI);

        String email = null;
        String jwtToken = null;

        // Verificar si el header tiene un token JWT válido (empieza con "Bearer ")
        if (StringUtils.hasText(requestTokenHeader) && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7); // Extraer el token (despues de "Bearer ")
            log.debug("JWT Token received: {}", jwtToken); // Log el token recibido (¡evitar mostrar el token en producción!)
            try {
                // Extraer el email del token JWT
                email = jwtTokenUtil.extractEmail(jwtToken);
                log.debug("Email extracted from token: {}", email);
            } catch (io.jsonwebtoken.ExpiredJwtException e) {
                // Si el token está expirado, loguearlo como advertencia
                log.warn("JWT Token has expired: {}", e.getMessage());
            } catch (Exception e) {
                // Si ocurre un error al procesar el token, loguearlo como error
                log.error("Error processing JWT Token: {}", e.getMessage());
            }
        } else {
            // Si el header está vacío o no contiene un Bearer Token
            // Solo loguear las rutas que no sean públicas, como /api/auth/login
            if (!requestURI.startsWith("/api/auth") && !requestURI.equals("/login.html") /*...otras rutas públicas...*/) {
                log.warn("Authorization header missing or does not start with Bearer for request: {}", requestURI);
            }
        }

        // Si se ha extraído un email y no hay autenticación previa en el contexto de seguridad
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.debug("Attempting to load user details for email: {}", email);
            // Cargar los detalles del usuario desde el servicio personalizado
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);

            try {
                // Validar el token JWT
                if (jwtTokenUtil.validateToken(jwtToken, userDetails.getUsername())) {
                    log.debug("Token is valid for user: {}", email);
                    // Crear un objeto de autenticación con los detalles del usuario y las autoridades
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    // Establecer los detalles de la solicitud para la autenticación
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // Establecer la autenticación en el contexto de seguridad
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("User '{}' successfully authenticated for request: {}", email, requestURI);
                } else {
                    log.warn("Token validation failed for user: {}", email);
                }
            } catch (Exception e) {
                // Loguear cualquier error durante la validación del token
                log.error("Error during token validation for user {}: {}", email, e.getMessage());
            }
        } else {
            // Si no se ha extraído un email, loguearlo para solicitudes protegidas
            if (email == null && !requestURI.startsWith("/api/auth") /*...etc...*/) {
                log.debug("No email extracted from token for protected request: {}", requestURI);
            }
            // Si ya existe autenticación, no hacer nada extra
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                log.trace("Security context already holds authentication for '{}'", SecurityContextHolder.getContext().getAuthentication().getName());
            }
        }

        // Continuar con el siguiente filtro de la cadena
        chain.doFilter(request, response);
    }
}
