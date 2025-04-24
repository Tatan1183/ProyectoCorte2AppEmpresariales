package com.app.veterinaria.controller;

import com.app.veterinaria.dto.CitaDTO;
import com.app.veterinaria.service.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/citas") // Define la ruta base para las solicitudes relacionadas con citas
public class CitaController {

    @Autowired
    private CitaService citaService; // Servicio que maneja la lógica de negocio de las citas

    // Método para obtener todas las citas
    @GetMapping // Indica que este método maneja las solicitudes GET a la ruta "/api/citas"
    public List<CitaDTO> getAllCitas() {
        // Devuelve todas las citas mediante el servicio
        return citaService.findAll();
    }

    // Método para obtener una cita por su ID
    @GetMapping("/{id}") // Indica que este método maneja solicitudes GET a la ruta "/api/citas/{id}"
    public ResponseEntity<CitaDTO> getCitaById(@PathVariable Long id) {
        // Busca la cita por su ID y devuelve una respuesta con el objeto CitaDTO si existe, o una respuesta de "no encontrado" si no
        return citaService.findById(id)
                .map(ResponseEntity::ok) // Si la cita se encuentra, se retorna un 200 OK con los datos
                .orElseGet(() -> ResponseEntity.notFound().build()); // Si no se encuentra, retorna un 404 Not Found
    }

    // Método para crear una nueva cita
    @PostMapping // Indica que este método maneja solicitudes POST a la ruta "/api/citas"
    public ResponseEntity<CitaDTO> createCita(@RequestBody CitaDTO citaDTO) {
        // Guarda la nueva cita a través del servicio y devuelve la cita creada
        CitaDTO savedCita = citaService.save(citaDTO);
        return ResponseEntity.ok(savedCita); // Devuelve un 200 OK con la cita recién creada
    }

    // Método para actualizar una cita existente
    @PutMapping("/{id}") // Indica que este método maneja solicitudes PUT a la ruta "/api/citas/{id}"
    public ResponseEntity<CitaDTO> updateCita(@PathVariable Long id, @RequestBody CitaDTO citaDTO) {
        // Verifica si la cita existe antes de intentar actualizarla
        if (!citaService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build(); // Si no existe, retorna un 404 Not Found
        }

        // Asigna el ID de la cita a la citaDTO para asegurarse de que se actualice la cita correcta
        citaDTO.setId(id);
        // Guarda la cita actualizada
        CitaDTO updatedCita = citaService.save(citaDTO);
        return ResponseEntity.ok(updatedCita); // Retorna la cita actualizada con un 200 OK
    }

    // Método para eliminar una cita por su ID
    @DeleteMapping("/{id}") // Indica que este método maneja solicitudes DELETE a la ruta "/api/citas/{id}"
    public ResponseEntity<Void> deleteCita(@PathVariable Long id) {
        // Verifica si la cita existe antes de intentar eliminarla
        if (!citaService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build(); // Si no existe, retorna un 404 Not Found
        }

        // Elimina la cita usando el servicio
        citaService.deleteById(id);
        return ResponseEntity.noContent().build(); // Retorna un 204 No Content, indicando que la operación fue exitosa pero sin cuerpo de respuesta
    }
}
