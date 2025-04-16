package com.app.veterinaria.controller;

import com.app.veterinaria.model.Veterinario;
import com.app.veterinaria.service.VeterinarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/veterinarios")
public class VeterinarioController {

    @Autowired
    private VeterinarioService veterinarioService;

    @GetMapping
    public List<Veterinario> getAllVeterinarios() {
        return veterinarioService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Veterinario> getVeterinarioById(@PathVariable Long id) {
        return veterinarioService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Veterinario createVeterinario(@RequestBody Veterinario veterinario) {
        return veterinarioService.save(veterinario);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = veterinarioService.uploadImage(file);
            return ResponseEntity.ok().body(fileName);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error al subir la imagen: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Veterinario> updateVeterinario(@PathVariable Long id, @RequestBody Veterinario veterinarioDetails) {
        if (!veterinarioService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        veterinarioDetails.setId(id);
        Veterinario updatedVeterinario = veterinarioService.save(veterinarioDetails);
        return ResponseEntity.ok(updatedVeterinario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVeterinario(@PathVariable Long id) {
        if (!veterinarioService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        veterinarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
