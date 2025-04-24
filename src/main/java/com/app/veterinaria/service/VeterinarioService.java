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

@Service // Marca esta clase como un servicio de Spring, lo que permite que Spring la gestione como un componente
public class VeterinarioService {

    @Autowired // Inyección de dependencias para acceder al repositorio de veterinarios
    private VeterinarioRepository veterinarioRepository;

    @Autowired // Inyección de dependencias para el codificador de contraseñas
    private PasswordEncoder passwordEncoder;

    // Obtener todos los veterinarios de la base de datos
    public List<Veterinario> findAll() {
        return veterinarioRepository.findAll(); // Llama al método 'findAll' del repositorio para obtener todos los veterinarios
    }

    // Buscar un veterinario por su ID
    public Optional<Veterinario> findById(Long id) {
        return veterinarioRepository.findById(id); // Llama al método 'findById' del repositorio para buscar el veterinario por ID
    }

    // Crear o actualizar un veterinario
    @Transactional // Asegura que esta operación se ejecute en una transacción
    public Veterinario save(Veterinario veterinario) {
        Veterinario vetToSave;

        if (veterinario.getId() != null) {
            // --- ACTUALIZACIÓN --- Si el veterinario tiene un ID, se actualiza el registro existente
            Veterinario existingVet = veterinarioRepository.findById(veterinario.getId())
                    .orElseThrow(() -> new RuntimeException("Veterinario no encontrado con id: " + veterinario.getId()));

            // Actualizar los datos básicos del veterinario
            existingVet.setNombre(veterinario.getNombre());
            existingVet.setApellido(veterinario.getApellido());
            existingVet.setEspecialidad(veterinario.getEspecialidad());
            existingVet.setEmail(veterinario.getEmail());

            // Imagen: si no se proporciona una nueva, mantiene la imagen existente
            existingVet.setImagen(
                    (veterinario.getImagen() == null || veterinario.getImagen().trim().isEmpty())
                    ? existingVet.getImagen()
                    : veterinario.getImagen()
            );

            // Contraseña: se encripta solo si se proporciona una nueva
            if (veterinario.getPassword() != null && !veterinario.getPassword().trim().isEmpty()) {
                if (!passwordEncoder.matches(veterinario.getPassword(), existingVet.getPassword())) {
                    existingVet.setPassword(passwordEncoder.encode(veterinario.getPassword())); // Encripta la nueva contraseña
                }
            }

            vetToSave = existingVet; // Se prepara el veterinario actualizado para guardar

        } else {
            // --- CREACIÓN --- Si no tiene ID, se crea un nuevo veterinario
            vetToSave = veterinario;

            // Encriptar contraseña si se proporciona
            if (vetToSave.getPassword() != null && !vetToSave.getPassword().trim().isEmpty()) {
                vetToSave.setPassword(passwordEncoder.encode(vetToSave.getPassword())); // Encripta la contraseña antes de guardarla
            }
        }

        return veterinarioRepository.save(vetToSave); // Guarda el veterinario (nuevo o actualizado)
    }

    // Eliminar un veterinario por su ID
    public void deleteById(Long id) {
        veterinarioRepository.deleteById(id); // Llama al método 'deleteById' del repositorio para eliminar el veterinario por ID
    }

    // Guardar imagen en carpeta local y retornar el nombre del archivo
    public String uploadImage(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename(); // Obtiene el nombre original del archivo
        Path path = Paths.get("src/main/resources/static/images/" + fileName); // Define la ruta donde se guardará la imagen
        Files.write(path, file.getBytes()); // Escribe el contenido del archivo en el sistema de archivos
        return fileName; // Retorna el nombre del archivo guardado
    }
}
