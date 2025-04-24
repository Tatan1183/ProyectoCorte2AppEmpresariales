package com.app.veterinaria.service;

import com.app.veterinaria.model.Veterinario;
import com.app.veterinaria.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class VeterinarioService {

    @Autowired
    private VeterinarioRepository veterinarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Obtener todos los veterinarios
    public List<Veterinario> findAll() {
        return veterinarioRepository.findAll();
    }

    // Buscar veterinario por ID
    public Optional<Veterinario> findById(Long id) {
        return veterinarioRepository.findById(id);
    }

    // Crear o actualizar un veterinario
    @Transactional
    public Veterinario save(Veterinario veterinario) {
        Veterinario vetToSave;

        if (veterinario.getId() != null) {
            // --- ACTUALIZACIÓN ---
            Veterinario existingVet = veterinarioRepository.findById(veterinario.getId())
                    .orElseThrow(() -> new RuntimeException("Veterinario no encontrado con id: " + veterinario.getId()));

            // Actualizar datos básicos
            existingVet.setNombre(veterinario.getNombre());
            existingVet.setApellido(veterinario.getApellido());
            existingVet.setEspecialidad(veterinario.getEspecialidad());
            existingVet.setEmail(veterinario.getEmail());

            // Imagen: mantener la existente si no se proporciona una nueva
            existingVet.setImagen(
                    (veterinario.getImagen() == null || veterinario.getImagen().trim().isEmpty())
                            ? existingVet.getImagen()
                            : veterinario.getImagen()
            );

            // Contraseña: encriptar si se proporciona una nueva diferente
            if (veterinario.getPassword() != null && !veterinario.getPassword().trim().isEmpty()) {
                if (!passwordEncoder.matches(veterinario.getPassword(), existingVet.getPassword())) {
                    existingVet.setPassword(passwordEncoder.encode(veterinario.getPassword()));
                }
            }

            vetToSave = existingVet;

        } else {
            // --- CREACIÓN ---
            vetToSave = veterinario;

            // Encriptar contraseña si se proporciona
            if (vetToSave.getPassword() != null && !vetToSave.getPassword().trim().isEmpty()) {
                vetToSave.setPassword(passwordEncoder.encode(vetToSave.getPassword()));
            }
        }

        return veterinarioRepository.save(vetToSave);
    }

    // Eliminar un veterinario por ID
    public void deleteById(Long id) {
        veterinarioRepository.deleteById(id);
    }

    // Guardar imagen en carpeta local y retornar el nombre del archivo
    public String uploadImage(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/images/" + fileName);
        Files.write(path, file.getBytes());
        return fileName;
    }
}

