package com.app.veterinaria.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger; // Importar Logger
import org.slf4j.LoggerFactory; // Importar LoggerFactory
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils; // Helper para verificar strings
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    // Añadir logger
    private static final Logger log = LoggerFactory.getLogger(JwtTokenFilter.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");
        String requestURI = request.getRequestURI(); // Obtener la URI para logging

        log.debug("Processing request to: {}", requestURI); // Log URI

        String email = null;
        String jwtToken = null;

        // Verificar que el header no sea nulo y empiece con "Bearer "
        if (StringUtils.hasText(requestTokenHeader) && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            log.debug("JWT Token received: {}", jwtToken); // Log token recibido (¡cuidado en producción!)
            try {
                email = jwtTokenUtil.extractEmail(jwtToken);
                log.debug("Email extracted from token: {}", email);
            } catch (io.jsonwebtoken.ExpiredJwtException e) {
                log.warn("JWT Token has expired: {}", e.getMessage());
            } catch (Exception e) {
                log.error("Error processing JWT Token: {}", e.getMessage());
            }
        } else {
            // Solo loguear si no es una ruta pública esperada (como /api/auth/login)
            if (!requestURI.startsWith("/api/auth") && !requestURI.equals("/login.html") /*...otras rutas públicas...*/) {
                log.warn("Authorization header missing or does not start with Bearer for request: {}", requestURI);
            }
        }

        // Si tenemos email y no hay autenticación previa en el contexto
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.debug("Attempting to load user details for email: {}", email);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);

            try {
                if (jwtTokenUtil.validateToken(jwtToken, userDetails.getUsername())) {
                    log.debug("Token is valid for user: {}", email);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // Establecer la autenticación en el contexto
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("User '{}' successfully authenticated for request: {}", email, requestURI);
                } else {
                    log.warn("Token validation failed for user: {}", email);
                }
            } catch (Exception e) {
                log.error("Error during token validation for user {}: {}", email, e.getMessage());
            }
        } else {
            if (email == null && !requestURI.startsWith("/api/auth") /*...etc...*/) {
                log.debug("No email extracted from token for protected request: {}", requestURI);
            }
            // Si ya hay autenticación, no hacer nada extra
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                log.trace("Security context already holds authentication for '{}'", SecurityContextHolder.getContext().getAuthentication().getName());
            }
        }

        // Continuar con la cadena de filtros
        chain.doFilter(request, response);
    }
}
