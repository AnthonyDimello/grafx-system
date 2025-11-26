package com.grafx.system.service;

import com.grafx.system.model.Venda;
import com.grafx.system.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class VendaService {
    
    @Autowired
    private VendaRepository vendaRepository;
    
    public List<Venda> findAll() {
        return vendaRepository.findAll();
    }
    
    public Venda save(Venda venda) {
        return vendaRepository.save(venda);
    }
    
    public Optional<Venda> findById(Long id) {
        return vendaRepository.findById(id);
    }
    
    public void deleteById(Long id) {
        vendaRepository.deleteById(id);
    }
    
    public List<Venda> buscarPorCliente(String nomeCliente) {
        return vendaRepository.findByNomeClienteContainingIgnoreCase(nomeCliente);
    }
}
