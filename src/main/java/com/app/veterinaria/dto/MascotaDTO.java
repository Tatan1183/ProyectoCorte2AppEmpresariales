package com.app.veterinaria.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class MascotaDTO {

    private Long id;
    private String nombre;
    private String especie;
    private String raza;
    private LocalDate fechaNacimiento;
    private String imagen;
    private Long clienteId;
    private String clienteNombre;
    private String clienteApellido;
}
