package com.app.veterinaria.controller;

import com.app.veterinaria.model.Veterinario;
import com.app.veterinaria.service.VeterinarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/veterinarios") // Define la ruta base para las solicitudes relacionadas con los veterinarios
public class VeterinarioController {

    @Autowired
    private VeterinarioService veterinarioService; // Servicio que maneja la lógica de negocio de los veterinarios

    // Método para obtener todos los veterinarios
    @GetMapping // Indica que este método maneja solicitudes GET a la ruta "/api/veterinarios"
    public List<Veterinario> getAllVeterinarios() {
        // Devuelve todos los veterinarios mediante el servicio
        return veterinarioService.findAll();
    }

    // Método para obtener un veterinario por su ID
    @GetMapping("/{id}") // Indica que este método maneja solicitudes GET a la ruta "/api/veterinarios/{id}"
    public ResponseEntity<Veterinario> getVeterinarioById(@PathVariable Long id) {
        // Busca el veterinario por su ID y devuelve una respuesta con el objeto Veterinario si existe, o una respuesta de "no encontrado" si no
        return veterinarioService.findById(id)
                .map(ResponseEntity::ok) // Si el veterinario se encuentra, se retorna un 200 OK con los datos
                .orElseGet(() -> ResponseEntity.notFound().build()); // Si no se encuentra, retorna un 404 Not Found
    }

    // Método para crear un nuevo veterinario
    @PostMapping // Indica que este método maneja solicitudes POST a la ruta "/api/veterinarios"
    public Veterinario createVeterinario(@RequestBody Veterinario veterinario) {
        // Guarda el nuevo veterinario a través del servicio y devuelve el veterinario creado
        return veterinarioService.save(veterinario);
    }

    // Método para subir una imagen asociada a un veterinario
    @PostMapping("/upload") // Indica que este método maneja solicitudes POST a la ruta "/api/veterinarios/upload"
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // Llama al servicio para subir la imagen y obtener el nombre del archivo
            String fileName = veterinarioService.uploadImage(file);
            return ResponseEntity.ok().body(fileName); // Si la imagen se sube correctamente, retorna un 200 OK con el nombre del archivo
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error al subir la imagen: " + e.getMessage()); // Si ocurre un error, retorna un 400 Bad Request
        }
    }

    // Método para actualizar un veterinario existente
    @PutMapping("/{id}") // Indica que este método maneja solicitudes PUT a la ruta "/api/veterinarios/{id}"
    public ResponseEntity<Veterinario> updateVeterinario(@PathVariable Long id, @RequestBody Veterinario veterinarioDetails) {
        // Verifica si el veterinario existe antes de intentar actualizarlo
        if (!veterinarioService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build(); // Si no existe, retorna un 404 Not Found
        }

        // Asigna el ID del veterinario a los detalles del veterinario para asegurarse de que se actualice el veterinario correcto
        veterinarioDetails.setId(id);
        // Guarda el veterinario actualizado
        Veterinario updatedVeterinario = veterinarioService.save(veterinarioDetails);
        return ResponseEntity.ok(updatedVeterinario); // Retorna el veterinario actualizado con un 200 OK
    }

    // Método para eliminar un veterinario por su ID
    @DeleteMapping("/{id}") // Indica que este método maneja solicitudes DELETE a la ruta "/api/veterinarios/{id}"
    public ResponseEntity<Void> deleteVeterinario(@PathVariable Long id) {
        // Verifica si el veterinario existe antes de intentar eliminarlo
        if (!veterinarioService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build(); // Si no existe, retorna un 404 Not Found
        }

        // Elimina el veterinario utilizando el servicio
        veterinarioService.deleteById(id);
        return ResponseEntity.noContent().build(); // Retorna un 204 No Content, indicando que la operación fue exitosa pero sin cuerpo de respuesta
    }
}
