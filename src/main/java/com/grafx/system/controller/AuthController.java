package com.grafx.system.controller;

import com.grafx.system.service.AtendenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:8080")
public class AuthController {
    
    @Autowired
    private AtendenteService atendenteService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String usuario = credentials.get("usuario");
        String senha = credentials.get("senha");
        
        // Admin padrão
        if ("admin".equals(usuario) && "123".equals(senha)) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Login realizado com sucesso");
            return ResponseEntity.ok(response);
        }
        
        // Valida atendente
        if (atendenteService.validarLogin(usuario, senha)) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Login realizado com sucesso");
            return ResponseEntity.ok(response);
        }
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "Credenciais inválidas");
        return ResponseEntity.status(401).body(response);
    }
}
