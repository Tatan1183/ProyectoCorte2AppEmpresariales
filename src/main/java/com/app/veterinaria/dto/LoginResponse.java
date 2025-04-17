package com.app.veterinaria.dto;

import com.app.veterinaria.model.Veterinario;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private Veterinario veterinario;
}
