package com.app.veterinaria.service;

import com.app.veterinaria.model.Cliente;
import com.app.veterinaria.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente save(Cliente cliente) {
        // Asegurarse de que no se envíen mascotas en la creación
        if (cliente.getMascotas() != null) {
            cliente.getMascotas().clear();
        }
        return clienteRepository.save(cliente);
    }

    public Cliente update(Long id, Cliente clienteDetails) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        cliente.setNombre(clienteDetails.getNombre());
        cliente.setApellido(clienteDetails.getApellido());
        cliente.setTelefono(clienteDetails.getTelefono());
        cliente.setEmail(clienteDetails.getEmail());
        cliente.setDireccion(clienteDetails.getDireccion());

        return clienteRepository.save(cliente);
    }

    public void deleteById(Long id) {
        clienteRepository.deleteById(id);
    }
}
