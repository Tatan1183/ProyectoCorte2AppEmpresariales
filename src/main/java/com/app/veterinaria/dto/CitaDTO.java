package com.app.veterinaria.dto;

import lombok.Data; 
import java.time.LocalDateTime; // Importa la clase LocalDateTime para manejar la fecha y hora

@Data // Lombok genera automáticamente los métodos getters, setters, equals, hashCode y toString

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
