package com.app.veterinaria.controller;

import com.app.veterinaria.model.Servicio;
import com.app.veterinaria.service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/servicios") // Define la ruta base para las solicitudes relacionadas con los servicios
public class ServicioController {

    @Autowired
    private ServicioService servicioService; // Servicio que maneja la lógica de negocio de los servicios

    // Método para obtener todos los servicios
    @GetMapping // Indica que este método maneja las solicitudes GET a la ruta "/api/servicios"
    public List<Servicio> getAllServicios() {
        // Devuelve todos los servicios mediante el servicio
        return servicioService.findAll();
    }

    // Método para obtener un servicio por su ID
    @GetMapping("/{id}") // Indica que este método maneja solicitudes GET a la ruta "/api/servicios/{id}"
    public ResponseEntity<Servicio> getServicioById(@PathVariable Long id) {
        // Busca el servicio por su ID y devuelve una respuesta con el objeto Servicio si existe, o una respuesta de "no encontrado" si no
        return servicioService.findById(id)
                .map(ResponseEntity::ok) // Si el servicio se encuentra, se retorna un 200 OK con los datos
                .orElseGet(() -> ResponseEntity.notFound().build()); // Si no se encuentra, retorna un 404 Not Found
    }

    // Método para crear un nuevo servicio
    @PostMapping // Indica que este método maneja solicitudes POST a la ruta "/api/servicios"
    public Servicio createServicio(@RequestBody Servicio servicio) {
        // Guarda el nuevo servicio a través del servicio y devuelve el servicio creado
        return servicioService.save(servicio);
    }

    // Método para actualizar un servicio existente
    @PutMapping("/{id}") // Indica que este método maneja solicitudes PUT a la ruta "/api/servicios/{id}"
    public ResponseEntity<Servicio> updateServicio(@PathVariable Long id, @RequestBody Servicio servicioDetails) {
        // Verifica si el servicio existe antes de intentar actualizarlo
        if (!servicioService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build(); // Si no existe, retorna un 404 Not Found
        }

        // Asigna el ID del servicio a los detalles del servicio para asegurarse de que se actualice el servicio correcto
        servicioDetails.setId(id);
        // Guarda el servicio actualizado
        Servicio updatedServicio = servicioService.save(servicioDetails);
        return ResponseEntity.ok(updatedServicio); // Retorna el servicio actualizado con un 200 OK
    }

    // Método para eliminar un servicio por su ID
    @DeleteMapping("/{id}") // Indica que este método maneja solicitudes DELETE a la ruta "/api/servicios/{id}"
    public ResponseEntity<Void> deleteServicio(@PathVariable Long id) {
        // Verifica si el servicio existe antes de intentar eliminarlo
        if (!servicioService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build(); // Si no existe, retorna un 404 Not Found
        }

        // Elimina el servicio utilizando el servicio
        servicioService.deleteById(id);
        return ResponseEntity.noContent().build(); // Retorna un 204 No Content, indicando que la operación fue exitosa pero sin cuerpo de respuesta
    }
}
