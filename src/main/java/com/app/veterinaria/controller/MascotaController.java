package com.app.veterinaria.controller;

import com.app.veterinaria.dto.MascotaDTO;
import com.app.veterinaria.service.MascotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/mascotas")
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;

    @GetMapping
    public List<MascotaDTO> getAllMascotas() {
        return mascotaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MascotaDTO> getMascotaById(@PathVariable Long id) {
        return mascotaService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MascotaDTO> createMascota(@RequestBody MascotaDTO mascotaDTO) {
        MascotaDTO savedMascota = mascotaService.save(mascotaDTO);
        return ResponseEntity.ok(savedMascota);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = mascotaService.uploadImage(file);
            return ResponseEntity.ok().body(fileName);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error al subir la imagen: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MascotaDTO> updateMascota(@PathVariable Long id, @RequestBody MascotaDTO mascotaDTO) {
        if (!mascotaService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        mascotaDTO.setId(id);
        MascotaDTO updatedMascota = mascotaService.save(mascotaDTO);
        return ResponseEntity.ok(updatedMascota);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMascota(@PathVariable Long id) {
        if (!mascotaService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        mascotaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
