package com.app.veterinaria.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration // Anotación que indica que esta clase contiene configuraciones para el contexto de Spring
public class JacksonConfig {

    @Bean // Anotación que indica que este método define un bean para el contexto de Spring
    @Primary // Marca este bean como el principal a ser utilizado en caso de que haya múltiples opciones
    public ObjectMapper objectMapper() {
        // Crear una nueva instancia de ObjectMapper, que se usa para convertir objetos Java a JSON y viceversa
        ObjectMapper objectMapper = new ObjectMapper();

        // Registrar el módulo JavaTimeModule para manejar correctamente los tipos de fecha/hora en Java (como LocalDate, LocalDateTime)
        objectMapper.registerModule(new JavaTimeModule());

        // Deshabilitar la serialización de fechas como timestamps (en lugar de representarlas como cadenas ISO 8601)
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Devolver el ObjectMapper configurado
        return objectMapper;
    }
}
