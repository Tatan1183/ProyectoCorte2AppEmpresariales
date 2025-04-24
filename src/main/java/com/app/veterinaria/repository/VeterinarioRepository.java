package com.app.veterinaria.repository;

import com.app.veterinaria.model.Veterinario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.stereotype.Repository; 

@Repository // Marca esta interfaz como un repositorio Spring, lo que permite inyectarla en otros componentes
public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {

    // Método para buscar un veterinario por su correo electrónico
    Optional<Veterinario> findByEmail(String email);
}

