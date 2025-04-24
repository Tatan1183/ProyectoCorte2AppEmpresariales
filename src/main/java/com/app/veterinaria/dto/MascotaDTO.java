package com.app.veterinaria.dto;

import lombok.Data;
import java.time.LocalDate; // Importa la clase LocalDate para manejar la fecha de nacimiento de la mascota

@Data // Lombok genera automáticamente los métodos getters, setters, equals, hashCode y toString
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
