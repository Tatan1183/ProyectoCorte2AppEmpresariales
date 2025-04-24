package com.app.veterinaria.security;

import com.app.veterinaria.model.Veterinario;
import com.app.veterinaria.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service // Marca la clase como un servicio gestionado por Spring
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired // Inyección automática del repositorio de Veterinarios
    private VeterinarioRepository veterinarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Buscar veterinario por email
        Veterinario veterinario = veterinarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        // Crear un objeto User de Spring Security con el email, contraseña y roles (vacío en este caso)
        return new User(veterinario.getEmail(), veterinario.getPassword(), new ArrayList<>());
    }
}
