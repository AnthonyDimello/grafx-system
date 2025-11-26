package com.grafx.system.repository;

import com.grafx.system.model.Atendente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AtendenteRepository extends JpaRepository<Atendente, Long> {
    Optional<Atendente> findByCpf(String cpf);
    Optional<Atendente> findByNomeAndSenha(String nome, String senha);
    boolean existsByCpf(String cpf);
}
