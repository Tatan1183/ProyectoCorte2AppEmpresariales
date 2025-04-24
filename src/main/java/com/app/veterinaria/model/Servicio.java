package com.app.veterinaria.model;

import jakarta.persistence.*; // Importa las clases necesarias para JPA (Java Persistence API)
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal; // Para manejar el precio con precisión (números decimales)

@Entity // Indica que esta clase es una entidad JPA, que será mapeada a una tabla en la base de datos
@Table(name = "servicios") // Define el nombre de la tabla en la base de datos
@Data // Lombok genera automáticamente los métodos getters, setters, equals, hashCode y toString
@NoArgsConstructor // Lombok genera el constructor vacío
@AllArgsConstructor // Lombok genera el constructor con todos los campos
public class Servicio {

    @Id // Indica que este campo es la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Genera el valor automáticamente con autoincremento
    private Long id; // Identificador único para cada servicio

    private String nombre; // Nombre del servicio (por ejemplo, consulta, cirugía, vacunación)
    private String descripcion; // Descripción detallada del servicio
    private BigDecimal precio; // Precio del servicio, utilizando BigDecimal para manejar decimales con precisión
}
