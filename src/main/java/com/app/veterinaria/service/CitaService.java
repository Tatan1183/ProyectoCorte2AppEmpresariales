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

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private VeterinarioRepository veterinarioRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    public List<CitaDTO> findAll() {
        List<Cita> citas = citaRepository.findAll();
        return citas.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<CitaDTO> findById(Long id) {
        return citaRepository.findById(id).map(this::convertToDTO);
    }

    public CitaDTO save(CitaDTO citaDTO) {
        Cita cita = convertToEntity(citaDTO);
        Cita savedCita = citaRepository.save(cita);
        return convertToDTO(savedCita);
    }

    public void deleteById(Long id) {
        citaRepository.deleteById(id);
    }

    public CitaDTO convertToDTO(Cita cita) {
        CitaDTO dto = new CitaDTO();
        dto.setId(cita.getId());

        if (cita.getMascota() != null) {
            dto.setMascotaId(cita.getMascota().getId());
            dto.setMascotaNombre(cita.getMascota().getNombre());
        }

        if (cita.getVeterinario() != null) {
            dto.setVeterinarioId(cita.getVeterinario().getId());
            dto.setVeterinarioNombre(cita.getVeterinario().getNombre());
            dto.setVeterinarioApellido(cita.getVeterinario().getApellido());
        }

        if (cita.getServicio() != null) {
            dto.setServicioId(cita.getServicio().getId());
            dto.setServicioNombre(cita.getServicio().getNombre());
        }

        dto.setFechaHora(cita.getFechaHora());
        dto.setEstado(cita.getEstado());
        dto.setNotas(cita.getNotas());

        return dto;
    }

    public Cita convertToEntity(CitaDTO dto) {
        Cita cita = new Cita();
        cita.setId(dto.getId());

        if (dto.getMascotaId() != null) {
            Mascota mascota = mascotaRepository.findById(dto.getMascotaId())
                    .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
            cita.setMascota(mascota);
        }

        if (dto.getVeterinarioId() != null) {
            Veterinario veterinario = veterinarioRepository.findById(dto.getVeterinarioId())
                    .orElseThrow(() -> new RuntimeException("Veterinario no encontrado"));
            cita.setVeterinario(veterinario);
        }

        if (dto.getServicioId() != null) {
            Servicio servicio = servicioRepository.findById(dto.getServicioId())
                    .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
            cita.setServicio(servicio);
        }

        cita.setFechaHora(dto.getFechaHora());
        cita.setEstado(dto.getEstado());
        cita.setNotas(dto.getNotas());

        return cita;
    }
}
