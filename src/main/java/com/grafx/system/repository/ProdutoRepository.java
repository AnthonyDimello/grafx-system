package com.grafx.system.repository;

import com.grafx.system.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Optional<Produto> findByDescricao(String descricao);
    boolean existsByDescricao(String descricao);
}
