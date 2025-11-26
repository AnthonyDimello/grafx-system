package com.grafx.system.controller;

import com.grafx.system.model.Atendente;
import com.grafx.system.service.AtendenteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/atendentes")
@CrossOrigin(origins = "http://localhost:8080")
public class AtendenteController {
    
    @Autowired
    private AtendenteService atendenteService;
    
    @GetMapping
    public List<Atendente> getAllAtendentes() {
        return atendenteService.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Atendente> getAtendenteById(@PathVariable Long id) {
        Optional<Atendente> atendente = atendenteService.findById(id);
        return atendente.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<?> createAtendente(@Valid @RequestBody Atendente atendente) {
        // Verifica se CPF já existe
        if (atendenteService.existsByCpf(atendente.getCpf())) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Já existe um atendente cadastrado com este CPF");
            return ResponseEntity.badRequest().body(response);
        }
        
        Atendente savedAtendente = atendenteService.save(atendente);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Atendente cadastrado com sucesso");
        response.put("data", savedAtendente);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAtendente(@PathVariable Long id, 
                                           @Valid @RequestBody Atendente atendenteDetails) {
        Optional<Atendente> atendente = atendenteService.findById(id);
        if (atendente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Atendente existingAtendente = atendente.get();
        existingAtendente.setNome(atendenteDetails.getNome());
        existingAtendente.setTelefone(atendenteDetails.getTelefone());
        existingAtendente.setSenha(atendenteDetails.getSenha());
        
        Atendente updatedAtendente = atendenteService.save(existingAtendente);
        return ResponseEntity.ok(updatedAtendente);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAtendente(@PathVariable Long id) {
        if (atendenteService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        atendenteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}