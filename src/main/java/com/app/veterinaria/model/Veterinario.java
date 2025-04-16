package com.app.veterinaria.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "veterinarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Veterinario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private String especialidad;
    private String email;
    private String password;
    private String imagen;

    @OneToMany(mappedBy = "veterinario", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "veterinario-cita")
    private List<Cita> citas;
}
