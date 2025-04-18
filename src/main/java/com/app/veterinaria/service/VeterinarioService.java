package com.app.veterinaria.service;

import com.app.veterinaria.model.Veterinario;
import com.app.veterinaria.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importa la anotación transaccional
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

    public List<Veterinario> findAll() {
        return veterinarioRepository.findAll();
    }

    public Optional<Veterinario> findById(Long id) {
        return veterinarioRepository.findById(id);
    }

    @Transactional
    public Veterinario save(Veterinario veterinario) {
        Veterinario vetToSave;
        String imagenExistente = null;
        String passwordExistente = null;

        if (veterinario.getId() != null) {
            // --- EDITANDO ---
            Veterinario existingVet = veterinarioRepository.findById(veterinario.getId())
                    .orElseThrow(() -> new RuntimeException("Veterinario no encontrado para actualizar con id: " + veterinario.getId()));
            imagenExistente = existingVet.getImagen();
            passwordExistente = existingVet.getPassword();
            vetToSave = existingVet; // Trabajar sobre la entidad existente

            // Actualizar campos básicos
            vetToSave.setNombre(veterinario.getNombre());
            vetToSave.setApellido(veterinario.getApellido());
            vetToSave.setEspecialidad(veterinario.getEspecialidad());
            vetToSave.setEmail(veterinario.getEmail());

            // Manejar Imagen
            if (veterinario.getImagen() == null || veterinario.getImagen().trim().isEmpty()) {
                vetToSave.setImagen(imagenExistente); // Restaurar imagen si no vino nueva
            } else {
                vetToSave.setImagen(veterinario.getImagen()); // Usar la nueva imagen que vino
            }

            // Manejar Contraseña
            if (veterinario.getPassword() != null && !veterinario.getPassword().trim().isEmpty()) {
                // Si se proporcionó una nueva contraseña, encriptarla si es diferente
                if (!passwordEncoder.matches(veterinario.getPassword(), passwordExistente)) {
                    vetToSave.setPassword(passwordEncoder.encode(veterinario.getPassword()));
                }
                // Si es igual a la existente ya hasheada, no hacer nada.
            } else {
                // Si no se proporcionó contraseña nueva, mantener la existente
                vetToSave.setPassword(passwordExistente);
            }

        } else {
            // --- CREANDO ---
            vetToSave = veterinario;
            // Encriptar contraseña si es nueva y no nula
            if (vetToSave.getPassword() != null && !vetToSave.getPassword().trim().isEmpty()) {
                vetToSave.setPassword(passwordEncoder.encode(vetToSave.getPassword()));
            }
            // La imagen se establece directamente si viene, o será null si no.
        }

        return veterinarioRepository.save(vetToSave);
    }

    public void deleteById(Long id) {
        veterinarioRepository.deleteById(id);
    }

    public String uploadImage(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/images/" + fileName);
        Files.write(path, file.getBytes());
        return fileName;
    }
}

