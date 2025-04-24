package com.app.veterinaria.dto;

import lombok.Data;

@Data // Lombok para generar automáticamente los métodos getter, setter, toString, equals y hashCode
public class LoginRequest {

    private String email;
    private String password;
}
