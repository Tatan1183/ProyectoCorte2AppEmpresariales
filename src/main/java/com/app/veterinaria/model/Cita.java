package com.app.veterinaria.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "citas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cita {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "mascota_id")
    @JsonBackReference(value = "mascota-cita")
    private Mascota mascota;
    
    @ManyToOne
    @JoinColumn(name = "veterinario_id")
    @JsonBackReference(value = "veterinario-cita")
    private Veterinario veterinario;
    
    @ManyToOne
    @JoinColumn(name = "servicio_id")
    private Servicio servicio;
    
    private LocalDateTime fechaHora;
    private String estado;
    private String notas;
}