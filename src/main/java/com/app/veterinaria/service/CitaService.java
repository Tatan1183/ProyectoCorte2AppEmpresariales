package com.app.veterinaria.service;

import com.app.veterinaria.dto.CitaDTO;
import com.app.veterinaria.model.Cita;
import com.app.veterinaria.model.Mascota;
import com.app.veterinaria.model.Servicio;
import com.app.veterinaria.model.Veterinario;
import com.app.veterinaria.repository.CitaRepository;
import com.app.veterinaria.repository.MascotaRepository;
import com.app.veterinaria.repository.ServicioRepository;
import com.app.veterinaria.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service // Indica que esta clase es un servicio de Spring
public class CitaService {

    @Autowired
    private CitaRepository citaRepository; // Inyección de dependencia para el repositorio de citas

    @Autowired
    private MascotaRepository mascotaRepository; // Inyección de dependencia para el repositorio de mascotas

    @Autowired
    private VeterinarioRepository veterinarioRepository; // Inyección de dependencia para el repositorio de veterinarios

    @Autowired
    private ServicioRepository servicioRepository; // Inyección de dependencia para el repositorio de servicios

    // Obtener todas las citas y convertirlas a DTOs
    public List<CitaDTO> findAll() {
        List<Cita> citas = citaRepository.findAll(); // Obtener todas las citas de la base de datos
        return citas.stream().map(this::convertToDTO).collect(Collectors.toList()); // Convertir cada cita a DTO
    }

    // Obtener una cita por su ID y convertirla a DTO
    public Optional<CitaDTO> findById(Long id) {
        return citaRepository.findById(id).map(this::convertToDTO); // Buscar cita por ID y convertirla a DTO
    }

    // Guardar una nueva cita a partir de un CitaDTO y devolver el DTO de la cita guardada
    public CitaDTO save(CitaDTO citaDTO) {
        Cita cita = convertToEntity(citaDTO); // Convertir el DTO a entidad Cita
        Cita savedCita = citaRepository.save(cita); // Guardar la cita en la base de datos
        return convertToDTO(savedCita); // Convertir la cita guardada de vuelta a DTO
    }

    // Eliminar una cita por su ID
    public void deleteById(Long id) {
        citaRepository.deleteById(id); // Eliminar la cita de la base de datos
    }

    // Convertir una entidad Cita a un CitaDTO
    public CitaDTO convertToDTO(Cita cita) {
        CitaDTO dto = new CitaDTO(); // Crear un nuevo DTO para la cita
        dto.setId(cita.getId()); // Establecer el ID de la cita

        // Asignar los datos de la mascota, si existen
        if (cita.getMascota() != null) {
            dto.setMascotaId(cita.getMascota().getId());
            dto.setMascotaNombre(cita.getMascota().getNombre());
        }

        // Asignar los datos del veterinario, si existen
        if (cita.getVeterinario() != null) {
            dto.setVeterinarioId(cita.getVeterinario().getId());
            dto.setVeterinarioNombre(cita.getVeterinario().getNombre());
            dto.setVeterinarioApellido(cita.getVeterinario().getApellido());
        }

        // Asignar los datos del servicio, si existen
        if (cita.getServicio() != null) {
            dto.setServicioId(cita.getServicio().getId());
            dto.setServicioNombre(cita.getServicio().getNombre());
        }

        // Asignar los datos adicionales de la cita
        dto.setFechaHora(cita.getFechaHora());
        dto.setEstado(cita.getEstado());
        dto.setNotas(cita.getNotas());

        return dto; // Retornar el DTO de la cita
    }

    // Convertir un CitaDTO a una entidad Cita
    public Cita convertToEntity(CitaDTO dto) {
        Cita cita = new Cita(); // Crear una nueva entidad Cita
        cita.setId(dto.getId()); // Establecer el ID de la cita

        // Buscar y asignar la mascota, si se proporciona un ID
        if (dto.getMascotaId() != null) {
            Mascota mascota = mascotaRepository.findById(dto.getMascotaId())
                    .orElseThrow(() -> new RuntimeException("Mascota no encontrada")); // Lanzar excepción si no se encuentra
            cita.setMascota(mascota); // Asignar la mascota a la cita
        }

        // Buscar y asignar el veterinario, si se proporciona un ID
        if (dto.getVeterinarioId() != null) {
            Veterinario veterinario = veterinarioRepository.findById(dto.getVeterinarioId())
                    .orElseThrow(() -> new RuntimeException("Veterinario no encontrado")); // Lanzar excepción si no se encuentra
            cita.setVeterinario(veterinario); // Asignar el veterinario a la cita
        }

        // Buscar y asignar el servicio, si se proporciona un ID
        if (dto.getServicioId() != null) {
            Servicio servicio = servicioRepository.findById(dto.getServicioId())
                    .orElseThrow(() -> new RuntimeException("Servicio no encontrado")); // Lanzar excepción si no se encuentra
            cita.setServicio(servicio); // Asignar el servicio a la cita
        }

        // Asignar los datos adicionales de la cita
        cita.setFechaHora(dto.getFechaHora());
        cita.setEstado(dto.getEstado());
        cita.setNotas(dto.getNotas());

        return cita; // Retornar la entidad Cita
    }
}

