package com.grafx.system.controller;

import com.grafx.system.model.Atendente;
import com.grafx.system.model.Produto;
import com.grafx.system.model.Venda;
import com.grafx.system.service.VendaService;
import com.grafx.system.service.ProdutoService;
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
@RequestMapping("/api/vendas")
@CrossOrigin(origins = "http://localhost:8080")
public class VendaController {
    
    @Autowired
    private VendaService vendaService;
    
    @Autowired
    private ProdutoService produtoService;
    
    @Autowired
    private AtendenteService atendenteService;
    
    @GetMapping
    public List<Venda> getAllVendas() {
        return vendaService.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Venda> getVendaById(@PathVariable Long id) {
        Optional<Venda> venda = vendaService.findById(id);
        return venda.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<?> createVenda(@Valid @RequestBody Venda venda) {
        // Valida se produto existe
        if (venda.getProduto() == null || venda.getProduto().getId() == null) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Produto é obrigatório");
            return ResponseEntity.badRequest().body(response);
        }
        
        Optional<Produto> produto = produtoService.findById(venda.getProduto().getId());
        if (produto.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Produto não encontrado");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Valida se atendente existe
        if (venda.getAtendente() == null || venda.getAtendente().getId() == null) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Atendente é obrigatório");
            return ResponseEntity.badRequest().body(response);
        }
        
        Optional<Atendente> atendente = atendenteService.findById(venda.getAtendente().getId());
        if (atendente.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Atendente não encontrado");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Atualiza estoque do produto
        Produto produtoAtual = produto.get();
        if (produtoAtual.getEstoque() < venda.getQuantidade()) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Estoque insuficiente para o produto: " + produtoAtual.getDescricao());
            return ResponseEntity.badRequest().body(response);
        }
        
        produtoAtual.setEstoque(produtoAtual.getEstoque() - venda.getQuantidade());
        produtoService.save(produtoAtual);
        
        // Associa produto e atendente à venda
        venda.setProduto(produtoAtual);
        venda.setAtendente(atendente.get());
        
        Venda savedVenda = vendaService.save(venda);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Venda registrada com sucesso");
        response.put("data", savedVenda);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/cliente/{nome}")
    public List<Venda> getVendasPorCliente(@PathVariable String nome) {
        return vendaService.buscarPorCliente(nome);
    }
}
