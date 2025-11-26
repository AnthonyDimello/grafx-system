package com.grafx.system.repository;

import com.grafx.system.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
    List<Venda> findByOrderByDataVendaDesc();
    List<Venda> findByNomeClienteContainingIgnoreCase(String nomeCliente);
}
