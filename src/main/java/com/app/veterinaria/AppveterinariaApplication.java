package com.app.veterinaria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class AppveterinariaApplication {

	public static void main(String[] args) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String rawPassword = "password123"; // cambia según tu veterinario
            String encodedPassword = encoder.encode(rawPassword);
            System.out.println(encodedPassword);
		SpringApplication.run(AppveterinariaApplication.class, args);
        }
}
