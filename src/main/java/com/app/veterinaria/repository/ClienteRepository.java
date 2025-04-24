package com.app.veterinaria.repository;

import com.app.veterinaria.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Marca esta interfaz como un repositorio Spring, lo que permite inyectarla en otros componentes
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
