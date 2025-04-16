package com.app.veterinaria.controller;

import com.app.veterinaria.model.Servicio;
import com.app.veterinaria.service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

    @GetMapping
    public List<Servicio> getAllServicios() {
        return servicioService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servicio> getServicioById(@PathVariable Long id) {
        return servicioService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Servicio createServicio(@RequestBody Servicio servicio) {
        return servicioService.save(servicio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Servicio> updateServicio(@PathVariable Long id, @RequestBody Servicio servicioDetails) {
        if (!servicioService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        servicioDetails.setId(id);
        Servicio updatedServicio = servicioService.save(servicioDetails);
        return ResponseEntity.ok(updatedServicio);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServicio(@PathVariable Long id) {
        if (!servicioService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        servicioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
