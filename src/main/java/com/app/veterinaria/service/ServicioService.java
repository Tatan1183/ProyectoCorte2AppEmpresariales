package com.app.veterinaria.service;

import com.app.veterinaria.model.Servicio;
import com.app.veterinaria.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Marca esta clase como un servicio de Spring, lo que permite que Spring la gestione como un componente
public class ServicioService {

    @Autowired // Inyección de dependencias para acceder al repositorio de servicios
    private ServicioRepository servicioRepository;

    // Método para obtener todos los servicios de la base de datos
    public List<Servicio> findAll() {
        return servicioRepository.findAll(); // Llama al método 'findAll' del repositorio para obtener todos los registros
    }

    // Método para obtener un servicio específico por su ID
    public Optional<Servicio> findById(Long id) {
        return servicioRepository.findById(id); // Llama al método 'findById' del repositorio para buscar el servicio por ID
    }

    // Método para guardar o actualizar un servicio
    public Servicio save(Servicio servicio) {
        return servicioRepository.save(servicio); // Llama al método 'save' del repositorio para guardar o actualizar el servicio
    }

    // Método para eliminar un servicio por su ID
    public void deleteById(Long id) {
        servicioRepository.deleteById(id); // Llama al método 'deleteById' del repositorio para eliminar el servicio con el ID dado
    }
}

