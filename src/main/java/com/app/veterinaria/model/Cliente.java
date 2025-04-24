package com.app.veterinaria.model;

import com.fasterxml.jackson.annotation.JsonManagedReference; // Para evitar la referencia cíclica en la serialización JSON
import jakarta.persistence.*; // Importa las clases de JPA para el manejo de la base de datos
import lombok.AllArgsConstructor; 
import lombok.Data; 
import lombok.NoArgsConstructor; 

import java.util.List; // Para manejar las listas de mascotas asociadas al cliente

@Entity // Indica que esta clase es una entidad JPA, que será mapeada a una tabla de base de datos
@Table(name = "clientes") // Define el nombre de la tabla en la base de datos
@Data // Lombok genera automáticamente los métodos getters, setters, equals, hashCode y toString
@NoArgsConstructor // Genera el constructor vacío
@AllArgsConstructor // Genera el constructor con todos los campos
public class Cliente {

    @Id // Indica que este campo es la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Genera el valor automáticamente con autoincremento
    private Long id;

    private String nombre; 
    private String apellido; 
    private String telefono; 
    private String email; 
    private String direccion; 

    // Relación One-to-Many entre Cliente y Mascota (un cliente tiene muchas mascotas)
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL) // Define que las mascotas están relacionadas con el cliente
    @JsonManagedReference // Para evitar la referencia cíclica durante la serialización (en la relación inversa)
    private List<Mascota> mascotas; // Lista de mascotas asociadas al cliente
}
