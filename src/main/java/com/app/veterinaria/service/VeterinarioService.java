package com.app.veterinaria.service;

import com.app.veterinaria.model.Veterinario;
import com.app.veterinaria.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public List<Veterinario> findAll() {
        return veterinarioRepository.findAll();
    }

    public Optional<Veterinario> findById(Long id) {
        return veterinarioRepository.findById(id);
    }

    public Veterinario save(Veterinario veterinario) {
        return veterinarioRepository.save(veterinario);
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
