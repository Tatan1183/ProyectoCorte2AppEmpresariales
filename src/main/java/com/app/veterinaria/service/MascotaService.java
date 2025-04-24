package com.app.veterinaria.service;

import com.app.veterinaria.dto.MascotaDTO;
import com.app.veterinaria.model.Cliente;
import com.app.veterinaria.model.Mascota;
import com.app.veterinaria.repository.ClienteRepository;
import com.app.veterinaria.repository.MascotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MascotaService {

    @Autowired
    private MascotaRepository mascotaRepository; // Repositorio para interactuar con la base de datos de mascotas

    @Autowired
    private ClienteRepository clienteRepository; // Repositorio para interactuar con la base de datos de clientes

    // Obtener todas las mascotas y convertirlas en DTOs para ser usadas en la interfaz
    public List<MascotaDTO> findAll() {
        List<Mascota> mascotas = mascotaRepository.findAll();
        return mascotas.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Obtener una mascota por su ID y convertirla en DTO
    public Optional<MascotaDTO> findById(Long id) {
        return mascotaRepository.findById(id).map(this::convertToDTO);
    }

    // Guardar una nueva mascota o actualizar una existente
    public MascotaDTO save(MascotaDTO mascotaDTO) {
        Mascota mascota;

        // --- Verificar si es una actualización ---
        if (mascotaDTO.getId() != null) {
            // Buscar la mascota existente en la base de datos
            Mascota existingMascota = mascotaRepository.findById(mascotaDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Mascota no encontrada para actualizar con id: " + mascotaDTO.getId()));

            // Actualizar los atributos de la mascota
            mascota = existingMascota;
            mascota.setNombre(mascotaDTO.getNombre());
            mascota.setEspecie(mascotaDTO.getEspecie());
            mascota.setRaza(mascotaDTO.getRaza());
            mascota.setFechaNacimiento(mascotaDTO.getFechaNacimiento());

            // Si no se proporciona una nueva imagen, conservar la existente
            if (mascotaDTO.getImagen() == null || mascotaDTO.getImagen().trim().isEmpty()) {
                mascota.setImagen(existingMascota.getImagen());
            } else {
                mascota.setImagen(mascotaDTO.getImagen());
            }

            // Asociar cliente si se proporciona un nuevo cliente
            if (mascotaDTO.getClienteId() != null) {
                Cliente cliente = clienteRepository.findById(mascotaDTO.getClienteId())
                        .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
                mascota.setCliente(cliente);
            }

        } else {
            // --- Crear una nueva mascota ---
            mascota = convertToEntity(mascotaDTO);
        }

        // Guardar la mascota y convertirla de nuevo a DTO para la respuesta
        Mascota savedMascota = mascotaRepository.save(mascota);
        return convertToDTO(savedMascota);
    }

    // Eliminar una mascota por su ID
    public void deleteById(Long id) {
        mascotaRepository.deleteById(id);
    }

    // Subir una imagen de mascota y guardarla en el servidor
    public String uploadImage(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename(); // Obtener el nombre del archivo
        Path path = Paths.get("src/main/resources/static/images/" + fileName); // Definir la ruta donde se almacenará
        Files.write(path, file.getBytes()); // Escribir el archivo en la ruta
        return fileName; // Retornar el nombre del archivo
    }

    // Convertir una entidad Mascota a DTO (Data Transfer Object)
    public MascotaDTO convertToDTO(Mascota mascota) {
        MascotaDTO dto = new MascotaDTO();
        dto.setId(mascota.getId());
        dto.setNombre(mascota.getNombre());
        dto.setEspecie(mascota.getEspecie());
        dto.setRaza(mascota.getRaza());
        dto.setFechaNacimiento(mascota.getFechaNacimiento());
        dto.setImagen(mascota.getImagen());

        // Si la mascota tiene un cliente asociado, incluir los detalles del cliente en el DTO
        if (mascota.getCliente() != null) {
            dto.setClienteId(mascota.getCliente().getId());
            dto.setClienteNombre(mascota.getCliente().getNombre());
            dto.setClienteApellido(mascota.getCliente().getApellido());
        }

        return dto;
    }

    // Convertir un DTO de Mascota a la entidad Mascota
    public Mascota convertToEntity(MascotaDTO dto) {
        Mascota mascota = new Mascota();
        mascota.setId(dto.getId());
        mascota.setNombre(dto.getNombre());
        mascota.setEspecie(dto.getEspecie());
        mascota.setRaza(dto.getRaza());
        mascota.setFechaNacimiento(dto.getFechaNacimiento());
        mascota.setImagen(dto.getImagen());

        // Si se proporciona un cliente, asociarlo a la mascota
        if (dto.getClienteId() != null) {
            Cliente cliente = clienteRepository.findById(dto.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            mascota.setCliente(cliente);
        }

        return mascota;
    }
}
