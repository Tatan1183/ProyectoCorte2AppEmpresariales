package com.app.veterinaria.model;

import com.fasterxml.jackson.annotation.JsonBackReference; // Para evitar la referencia cíclica durante la serialización JSON
import jakarta.persistence.*; // Importa las clases de JPA para el manejo de la base de datos
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime; // Para manejar la fecha y hora de la cita

@Entity // Indica que esta clase es una entidad JPA, que será mapeada a una tabla de base de datos
@Table(name = "citas") // Define el nombre de la tabla en la base de datos
@Data // Lombok genera automáticamente los métodos getters, setters, equals, hashCode y toString
@NoArgsConstructor // Genera el constructor vacío
@AllArgsConstructor // Genera el constructor con todos los campos
public class Cita {

    @Id // Indica que este campo es la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Genera el valor automáticamente con autoincremento
    private Long id;

    // Relación Many-to-One entre Cita y Mascota (una cita pertenece a una mascota)
    @ManyToOne
    @JoinColumn(name = "mascota_id") // Columna que representa la relación con la tabla de mascotas
    @JsonBackReference(value = "mascota-cita") // Evita la referencia cíclica durante la serialización (en la relación inversa)
    private Mascota mascota;

    // Relación Many-to-One entre Cita y Veterinario (una cita pertenece a un veterinario)
    @ManyToOne
    @JoinColumn(name = "veterinario_id") // Columna que representa la relación con la tabla de veterinarios
    @JsonBackReference(value = "veterinario-cita") // Evita la referencia cíclica durante la serialización (en la relación inversa)
    private Veterinario veterinario;

    // Relación Many-to-One entre Cita y Servicio (una cita tiene un servicio asociado)
    @ManyToOne
    @JoinColumn(name = "servicio_id") // Columna que representa la relación con la tabla de servicios
    private Servicio servicio;

    private LocalDateTime fechaHora; // Fecha y hora de la cita
    private String estado; // Estado de la cita (ej. "Pendiente", "Completada")
    private String notas; // Notas adicionales sobre la cita
}
