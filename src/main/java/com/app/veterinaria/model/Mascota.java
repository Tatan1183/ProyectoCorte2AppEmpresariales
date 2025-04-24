package com.app.veterinaria.model;

import com.fasterxml.jackson.annotation.JsonBackReference; // Para evitar la referencia cíclica en la serialización JSON
import com.fasterxml.jackson.annotation.JsonManagedReference; // Para gestionar correctamente la relación en la serialización JSON
import jakarta.persistence.*; // Importa las clases de JPA para el manejo de la base de datos
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate; // Para manejar fechas en el campo fechaNacimiento
import java.util.List; // Para manejar las citas asociadas a la mascota

@Entity // Indica que esta clase es una entidad JPA, que será mapeada a una tabla de base de datos
@Table(name = "mascotas") // Define el nombre de la tabla en la base de datos
@Data // Lombok genera automáticamente los métodos getters, setters, equals, hashCode y toString
@NoArgsConstructor // Lombok genera el constructor vacío
@AllArgsConstructor // Lombok genera el constructor con todos los campos
public class Mascota {

    @Id // Indica que este campo es la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Genera el valor automáticamente con autoincremento
    private Long id;

    private String nombre;
    private String especie;
    private String raza;
    private LocalDate fechaNacimiento;
    private String imagen;

    // Relación Many-to-One con Cliente (una mascota pertenece a un cliente)
    @ManyToOne
    @JoinColumn(name = "cliente_id") // Establece la clave foránea para la relación
    @JsonBackReference // Para evitar la referencia cíclica en la serialización JSON
    private Cliente cliente; // Cliente al que pertenece la mascota

    // Relación One-to-Many con Cita (una mascota puede tener múltiples citas)
    @OneToMany(mappedBy = "mascota", cascade = CascadeType.ALL) // Mapeo con la clase Cita
    @JsonManagedReference(value = "mascota-cita") // Evita la referencia cíclica durante la serialización JSON
    private List<Cita> citas; // Lista de citas asociadas a la mascota
}
