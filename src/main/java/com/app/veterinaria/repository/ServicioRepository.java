package com.app.veterinaria.repository;

import com.app.veterinaria.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Marca esta interfaz como un repositorio Spring, lo que permite inyectarla en otros componentes
public interface ServicioRepository extends JpaRepository<Servicio, Long> {
}
