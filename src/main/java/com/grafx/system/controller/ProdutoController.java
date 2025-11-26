package com.grafx.system.controller;

import com.grafx.system.model.Produto;
import com.grafx.system.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = "http://localhost:8080")
public class ProdutoController {
    
    @Autowired
    private ProdutoService produtoService;
    
    @GetMapping
    public List<Produto> getAllProdutos() {
        return produtoService.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Produto> getProdutoById(@PathVariable Long id) {
        Optional<Produto> produto = produtoService.findById(id);
        return produto.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<?> createProduto(@Valid @RequestBody Produto produto) {
        // Verifica se produto com mesma descrição já existe
        if (produtoService.existsByDescricao(produto.getDescricao())) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Este produto/serviço já está cadastrado");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Garante que o valor é positivo
        if (produto.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Valor deve ser maior que zero");
            return ResponseEntity.badRequest().body(response);
        }
        
        Produto savedProduto = produtoService.save(produto);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Produto cadastrado com sucesso");
        response.put("data", savedProduto);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduto(@PathVariable Long id, 
                                         @Valid @RequestBody Produto produtoDetails) {
        Optional<Produto> produto = produtoService.findById(id);
        if (produto.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Produto existingProduto = produto.get();
        existingProduto.setDescricao(produtoDetails.getDescricao());
        existingProduto.setEstoque(produtoDetails.getEstoque());
        existingProduto.setValor(produtoDetails.getValor());
        
        Produto updatedProduto = produtoService.save(existingProduto);
        return ResponseEntity.ok(updatedProduto);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable Long id) {
        if (produtoService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        produtoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
