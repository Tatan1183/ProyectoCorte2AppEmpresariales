package com.app.veterinaria.controller;

import com.app.veterinaria.dto.MascotaDTO;
import com.app.veterinaria.service.MascotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/mascotas") // Define la ruta base para las solicitudes relacionadas con las mascotas
public class MascotaController {

    @Autowired
    private MascotaService mascotaService; // Servicio que maneja la lógica de negocio de las mascotas

    // Método para obtener todas las mascotas
    @GetMapping // Indica que este método maneja las solicitudes GET a la ruta "/api/mascotas"
    public List<MascotaDTO> getAllMascotas() {
        // Devuelve todas las mascotas mediante el servicio
        return mascotaService.findAll();
    }

    // Método para obtener una mascota por su ID
    @GetMapping("/{id}") // Indica que este método maneja solicitudes GET a la ruta "/api/mascotas/{id}"
    public ResponseEntity<MascotaDTO> getMascotaById(@PathVariable Long id) {
        // Busca la mascota por su ID y devuelve una respuesta con el objeto MascotaDTO si existe, o una respuesta de "no encontrado" si no
        return mascotaService.findById(id)
                .map(ResponseEntity::ok) // Si la mascota se encuentra, se retorna un 200 OK con los datos
                .orElseGet(() -> ResponseEntity.notFound().build()); // Si no se encuentra, retorna un 404 Not Found
    }

    // Método para crear una nueva mascota
    @PostMapping // Indica que este método maneja solicitudes POST a la ruta "/api/mascotas"
    public ResponseEntity<MascotaDTO> createMascota(@RequestBody MascotaDTO mascotaDTO) {
        // Guarda la nueva mascota a través del servicio y devuelve la mascota creada
        MascotaDTO savedMascota = mascotaService.save(mascotaDTO);
        return ResponseEntity.ok(savedMascota); // Retorna un 200 OK con la mascota recién creada
    }

    // Método para subir una imagen de una mascota
    @PostMapping("/upload") // Indica que este método maneja solicitudes POST a la ruta "/api/mascotas/upload"
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // Llama al servicio para subir la imagen y obtener el nombre del archivo
            String fileName = mascotaService.uploadImage(file);
            return ResponseEntity.ok().body(fileName); // Retorna el nombre del archivo con un 200 OK
        } catch (IOException e) {
            // Si ocurre un error al subir la imagen, se retorna un 400 Bad Request con el mensaje de error
            return ResponseEntity.badRequest().body("Error al subir la imagen: " + e.getMessage());
        }
    }

    // Método para actualizar una mascota existente
    @PutMapping("/{id}") // Indica que este método maneja solicitudes PUT a la ruta "/api/mascotas/{id}"
    public ResponseEntity<MascotaDTO> updateMascota(@PathVariable Long id, @RequestBody MascotaDTO mascotaDTO) {
        // Verifica si la mascota existe antes de intentar actualizarla
        if (!mascotaService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build(); // Si no existe, retorna un 404 Not Found
        }

        // Asigna el ID de la mascota a la mascotaDTO para asegurarse de que se actualice la mascota correcta
        mascotaDTO.setId(id);
        // Guarda la mascota actualizada
        MascotaDTO updatedMascota = mascotaService.save(mascotaDTO);
        return ResponseEntity.ok(updatedMascota); // Retorna la mascota actualizada con un 200 OK
    }

    // Método para eliminar una mascota por su ID
    @DeleteMapping("/{id}") // Indica que este método maneja solicitudes DELETE a la ruta "/api/mascotas/{id}"
    public ResponseEntity<Void> deleteMascota(@PathVariable Long id) {
        // Verifica si la mascota existe antes de intentar eliminarla
        if (!mascotaService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build(); // Si no existe, retorna un 404 Not Found
        }

        // Elimina la mascota usando el servicio
        mascotaService.deleteById(id);
        return ResponseEntity.noContent().build(); // Retorna un 204 No Content, indicando que la operación fue exitosa pero sin cuerpo de respuesta
    }
}
