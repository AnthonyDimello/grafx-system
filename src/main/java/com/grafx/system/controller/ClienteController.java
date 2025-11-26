package com.grafx.system.controller;

import com.grafx.system.model.Cliente;
import com.grafx.system.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "http://localhost:8080")
public class ClienteController {
    
    @Autowired
    private ClienteService clienteService;
    
    @GetMapping
    public List<Cliente> getAllClientes() {
        return clienteService.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteService.findById(id);
        return cliente.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<?> createCliente(@Valid @RequestBody Cliente cliente) {
        // Verifica se CPF já existe
        if (clienteService.existsByCpf(cliente.getCpf())) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Já existe um cliente cadastrado com este CPF");
            return ResponseEntity.badRequest().body(response);
        }
        
        Cliente savedCliente = clienteService.save(cliente);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Cliente cadastrado com sucesso");
        response.put("data", savedCliente);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCliente(@PathVariable Long id, 
                                         @Valid @RequestBody Cliente clienteDetails) {
        Optional<Cliente> cliente = clienteService.findById(id);
        if (cliente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Cliente existingCliente = cliente.get();
        existingCliente.setNome(clienteDetails.getNome());
        existingCliente.setTelefone(clienteDetails.getTelefone());
        existingCliente.setEmail(clienteDetails.getEmail());
        existingCliente.setEndereco(clienteDetails.getEndereco());
        
        Cliente updatedCliente = clienteService.save(existingCliente);
        return ResponseEntity.ok(updatedCliente);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        if (clienteService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        clienteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
