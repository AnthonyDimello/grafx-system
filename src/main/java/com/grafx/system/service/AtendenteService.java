package com.grafx.system.service;

import com.grafx.system.model.Atendente;
import com.grafx.system.repository.AtendenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AtendenteService {
    
    @Autowired
    private AtendenteRepository atendenteRepository;
    
    public List<Atendente> findAll() {
        return atendenteRepository.findAll();
    }
    
    public Optional<Atendente> findById(Long id) {
        return atendenteRepository.findById(id);
    }
    
    public Atendente save(Atendente atendente) {
        return atendenteRepository.save(atendente);
    }
    
    public void deleteById(Long id) {
        atendenteRepository.deleteById(id);
    }
    
    public boolean existsByCpf(String cpf) {
        return atendenteRepository.existsByCpf(cpf);
    }
    
    public boolean validarLogin(String nome, String senha) {
        return atendenteRepository.findByNomeAndSenha(nome, senha).isPresent();
    }
}
