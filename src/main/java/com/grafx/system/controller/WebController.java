package com.grafx.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/index.html")
    public String indexHtml() {
        return "index";
    }
    
    @GetMapping("/menu")
    public String menu() {
        return "menu";
    }
    
    @GetMapping("/menu.html")
    public String menuHtml() {
        return "menu";
    }
    
    @GetMapping("/cliente")
    public String cliente() {
        return "cliente";
    }
    
    @GetMapping("/cliente.html")
    public String clienteHtml() {
        return "cliente";
    }
    
    @GetMapping("/produto")
    public String produto() {
        return "produto";
    }
    
    @GetMapping("/produto.html")
    public String produtoHtml() {
        return "produto";
    }
    
    @GetMapping("/venda")
    public String venda() {
        return "venda";
    }
    
    @GetMapping("/venda.html")
    public String vendaHtml() {
        return "venda";
    }
    
    @GetMapping("/atendente")
    public String atendente() {
        return "atendente";
    }
    
    @GetMapping("/atendente.html")
    public String atendenteHtml() {
        return "atendente";
    }
    
    @GetMapping("/busca")
    public String busca() {
        return "busca";
    }
    
    @GetMapping("/busca.html")
    public String buscaHtml() {
        return "busca";
    }
}
