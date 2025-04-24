package com.app.veterinaria.controller;

import com.app.veterinaria.model.Cliente;
import com.app.veterinaria.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/clientes") // Define la ruta base para las solicitudes relacionadas con los clientes
public class ClienteController {

    @Autowired
    private ClienteService clienteService; // Servicio que maneja la lógica de negocio de los clientes

    // Método para obtener todos los clientes
    @GetMapping // Indica que este método maneja las solicitudes GET a la ruta "/api/clientes"
    public List<Cliente> getAllClientes() {
        // Devuelve todos los clientes mediante el servicio
        return clienteService.findAll();
    }

    // Método para obtener un cliente por su ID
    @GetMapping("/{id}") // Indica que este método maneja solicitudes GET a la ruta "/api/clientes/{id}"
    public ResponseEntity<Cliente> getClienteById(@PathVariable Long id) {
        // Busca el cliente por su ID y devuelve una respuesta con el objeto Cliente si existe, o una respuesta de "no encontrado" si no
        return clienteService.findById(id)
                .map(ResponseEntity::ok) // Si el cliente se encuentra, se retorna un 200 OK con los datos
                .orElseGet(() -> ResponseEntity.notFound().build()); // Si no se encuentra, retorna un 404 Not Found
    }

    // Método para crear un nuevo cliente
    @PostMapping // Indica que este método maneja solicitudes POST a la ruta "/api/clientes"
    public Cliente createCliente(@RequestBody Cliente cliente) {
        // Guarda el nuevo cliente a través del servicio y lo devuelve
        return clienteService.save(cliente);
    }

    // Método para actualizar un cliente existente
    @PutMapping("/{id}") // Indica que este método maneja solicitudes PUT a la ruta "/api/clientes/{id}"
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long id, @RequestBody Cliente clienteDetails) {
        // Verifica si el cliente existe antes de intentar actualizarlo
        if (!clienteService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build(); // Si no existe, retorna un 404 Not Found
        }

        // Actualiza el cliente con los nuevos detalles
        Cliente updatedCliente = clienteService.update(id, clienteDetails);
        return ResponseEntity.ok(updatedCliente); // Retorna el cliente actualizado con un 200 OK
    }

    
    // Método para eliminar un cliente por su ID
    @DeleteMapping("/{id}") // Indica que este método maneja solicitudes DELETE a la ruta "/api/clientes/{id}"
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        // Verifica si el cliente existe antes de intentar eliminarlo
        if (!clienteService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build(); // Si no existe, retorna un 404 Not Found
        }

        // Elimina el cliente usando el servicio
        clienteService.deleteById(id);
        return ResponseEntity.noContent().build(); // Retorna un 204 No Content, indicando que la operación fue exitosa pero sin cuerpo de respuesta
    }
}

