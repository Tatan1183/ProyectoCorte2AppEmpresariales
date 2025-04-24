package com.app.veterinaria.dto;

import com.app.veterinaria.model.Veterinario;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data // Lombok genera automáticamente los métodos getters, setters, equals, hashCode y toString
@AllArgsConstructor // Lombok genera un constructor con todos los campos
public class LoginResponse {

    private String token; // El token JWT generado para el veterinario autenticado
    private Veterinario veterinario; // El objeto veterinario que contiene la información del veterinario autenticado
}
