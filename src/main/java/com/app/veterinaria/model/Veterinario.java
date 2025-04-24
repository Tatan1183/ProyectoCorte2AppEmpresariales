package com.app.veterinaria.model;

import com.fasterxml.jackson.annotation.JsonManagedReference; // Para gestionar las referencias circulares de JSON
import jakarta.persistence.*; // Importa las clases necesarias para JPA (Java Persistence API)
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List; // Importa la clase List para almacenar múltiples citas

@Entity // Indica que esta clase es una entidad JPA, que será mapeada a una tabla en la base de datos
@Table(name = "veterinarios") // Define el nombre de la tabla en la base de datos
@Data // Lombok genera automáticamente los métodos getters, setters, equals, hashCode y toString
@NoArgsConstructor // Lombok genera el constructor vacío
@AllArgsConstructor // Lombok genera el constructor con todos los campos
public class Veterinario {

    @Id // Indica que este campo es la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Genera el valor automáticamente con autoincremento
    private Long id; // Identificador único para cada veterinario

    private String nombre;
    private String apellido;
    private String especialidad;
    private String email;
    private String password;
    private String imagen;

    @OneToMany(mappedBy = "veterinario", cascade = CascadeType.ALL) // Relación uno a muchos con la entidad Cita
    @JsonManagedReference(value = "veterinario-cita") // Evita referencias circulares en la serialización JSON
    private List<Cita> citas; // Lista de citas que el veterinario tiene asignadas
}
