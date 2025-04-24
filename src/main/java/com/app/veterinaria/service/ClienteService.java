package com.app.veterinaria.service;

import com.app.veterinaria.model.Cliente;
import com.app.veterinaria.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Indica que esta clase es un servicio de Spring
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository; // Inyección de dependencia para el repositorio de clientes

    // Obtener todos los clientes de la base de datos
    public List<Cliente> findAll() {
        return clienteRepository.findAll(); // Retorna la lista de todos los clientes
    }

    // Obtener un cliente por su ID
    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id); // Retorna un cliente si se encuentra con el ID, o vacío si no
    }

    // Guardar un nuevo cliente en la base de datos
    public Cliente save(Cliente cliente) {
        // Asegurarse de que no se envíen mascotas en la creación (en caso de que el cliente tenga una lista de mascotas)
        if (cliente.getMascotas() != null) {
            cliente.getMascotas().clear(); // Limpiar la lista de mascotas antes de guardar
        }
        return clienteRepository.save(cliente); // Guardar el cliente en la base de datos
    }

    // Actualizar un cliente existente
    public Cliente update(Long id, Cliente clienteDetails) {
        // Buscar el cliente por ID y lanzar excepción si no se encuentra
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Actualizar los campos del cliente con los detalles proporcionados
        cliente.setNombre(clienteDetails.getNombre());
        cliente.setApellido(clienteDetails.getApellido());
        cliente.setTelefono(clienteDetails.getTelefono());
        cliente.setEmail(clienteDetails.getEmail());
        cliente.setDireccion(clienteDetails.getDireccion());

        // Guardar los cambios en la base de datos
        return clienteRepository.save(cliente);
    }

    // Eliminar un cliente por su ID
    public void deleteById(Long id) {
        clienteRepository.deleteById(id); // Eliminar el cliente de la base de datos
    }
}

