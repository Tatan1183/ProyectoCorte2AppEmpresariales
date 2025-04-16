package com.app.veterinaria.controller;

import com.app.veterinaria.dto.CitaDTO;
import com.app.veterinaria.service.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @GetMapping
    public List<CitaDTO> getAllCitas() {
        return citaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaDTO> getCitaById(@PathVariable Long id) {
        return citaService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CitaDTO> createCita(@RequestBody CitaDTO citaDTO) {
        CitaDTO savedCita = citaService.save(citaDTO);
        return ResponseEntity.ok(savedCita);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CitaDTO> updateCita(@PathVariable Long id, @RequestBody CitaDTO citaDTO) {
        if (!citaService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        citaDTO.setId(id);
        CitaDTO updatedCita = citaService.save(citaDTO);
        return ResponseEntity.ok(updatedCita);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCita(@PathVariable Long id) {
        if (!citaService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        citaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
