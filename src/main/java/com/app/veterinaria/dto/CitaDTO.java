package com.app.veterinaria.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CitaDTO {

    private Long id;
    private Long mascotaId;
    private String mascotaNombre;
    private Long veterinarioId;
    private String veterinarioNombre;
    private String veterinarioApellido;
    private Long servicioId;
    private String servicioNombre;
    private LocalDateTime fechaHora;
    private String estado;
    private String notas;
}
