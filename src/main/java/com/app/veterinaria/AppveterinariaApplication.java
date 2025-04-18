package com.app.veterinaria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppveterinariaApplication {

    public static void main(String[] args) {
        // ---- INICIO CÓDIGO A ELIMINAR ----
        //BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //String rawPassword = "vet123"; // Ya no es necesario para la operación normal
        //String encodedPassword = encoder.encode(rawPassword);
        //System.out.println("Hash de ejemplo (eliminar este bloque): " + encodedPassword);
        // ---- FIN CÓDIGO A ELIMINAR ----
        SpringApplication.run(AppveterinariaApplication.class, args);
    }
}
