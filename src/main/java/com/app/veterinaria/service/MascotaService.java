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
    private MascotaRepository mascotaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public List<MascotaDTO> findAll() {
        List<Mascota> mascotas = mascotaRepository.findAll();
        return mascotas.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<MascotaDTO> findById(Long id) {
        return mascotaRepository.findById(id).map(this::convertToDTO);
    }

    public MascotaDTO save(MascotaDTO mascotaDTO) {
        Mascota mascota = convertToEntity(mascotaDTO);
        Mascota savedMascota = mascotaRepository.save(mascota);
        return convertToDTO(savedMascota);
    }

    public void deleteById(Long id) {
        mascotaRepository.deleteById(id);
    }

    public String uploadImage(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/images/" + fileName);
        Files.write(path, file.getBytes());
        return fileName;
    }

    public MascotaDTO convertToDTO(Mascota mascota) {
        MascotaDTO dto = new MascotaDTO();
        dto.setId(mascota.getId());
        dto.setNombre(mascota.getNombre());
        dto.setEspecie(mascota.getEspecie());
        dto.setRaza(mascota.getRaza());
        dto.setFechaNacimiento(mascota.getFechaNacimiento());
        dto.setImagen(mascota.getImagen());

        if (mascota.getCliente() != null) {
            dto.setClienteId(mascota.getCliente().getId());
            dto.setClienteNombre(mascota.getCliente().getNombre());
            dto.setClienteApellido(mascota.getCliente().getApellido());
        }

        return dto;
    }

    public Mascota convertToEntity(MascotaDTO dto) {
        Mascota mascota = new Mascota();
        mascota.setId(dto.getId());
        mascota.setNombre(dto.getNombre());
        mascota.setEspecie(dto.getEspecie());
        mascota.setRaza(dto.getRaza());
        mascota.setFechaNacimiento(dto.getFechaNacimiento());
        mascota.setImagen(dto.getImagen());

        if (dto.getClienteId() != null) {
            Cliente cliente = clienteRepository.findById(dto.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            mascota.setCliente(cliente);
        }

        return mascota;
    }
}
