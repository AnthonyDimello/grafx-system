package com.grafx.system.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoValidationTest {
    
    private Validator validator;
    
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    @Test
    @DisplayName("CT010 - Validação de Produto Válido")
    void testProdutoValido() {
        // Given
        Produto produto = new Produto();
        produto.setDescricao("Mouse Gamer");
        produto.setEstoque(50);
        produto.setValor(new BigDecimal("89.90"));
        
        // When
        Set<ConstraintViolation<Produto>> violations = validator.validate(produto);
        
        // Then
        assertTrue(violations.isEmpty(), "Produto válido não deve ter violações");
    }
    
    @Test
    @DisplayName("CT011 - Validação de Produto Sem Descrição")
    void testProdutoSemDescricao() {
        // Given
        Produto produto = new Produto();
        produto.setDescricao(""); // Descrição vazia
        produto.setEstoque(50);
        produto.setValor(new BigDecimal("89.90"));
        
        // When
        Set<ConstraintViolation<Produto>> violations = validator.validate(produto);
        
        // Then
        assertFalse(violations.isEmpty(), "Produto sem descrição deve gerar violações");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("descricao")));
    }
    
    @Test
    @DisplayName("CT012 - Validação de Quantidade Negativa")
    void testQuantidadeNegativa() {
        // Given
        Produto produto = new Produto();
        produto.setDescricao("Mouse Gamer");
        produto.setEstoque(-5); // Estoque negativo
        produto.setValor(new BigDecimal("89.90"));
        
        // When
        Set<ConstraintViolation<Produto>> violations = validator.validate(produto);
        
        // Then
        assertFalse(violations.isEmpty(), "Estoque negativo deve gerar violações");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("estoque")));
    }
    
    @Test
    @DisplayName("CT013 - Validação de Preço Zero")
    void testPrecoZero() {
        // Given
        Produto produto = new Produto();
        produto.setDescricao("Mouse Gamer");
        produto.setEstoque(50);
        produto.setValor(BigDecimal.ZERO); // Preço zero
        
        // When
        Set<ConstraintViolation<Produto>> violations = validator.validate(produto);
        
        // Then
        assertFalse(violations.isEmpty(), "Preço zero deve gerar violações");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("valor")));
    }
    
    @Test
    @DisplayName("CT014 - Validação de Preço Negativo")
    void testPrecoNegativo() {
        // Given
        Produto produto = new Produto();
        produto.setDescricao("Mouse Gamer");
        produto.setEstoque(50);
        produto.setValor(new BigDecimal("-10.00")); // Preço negativo
        
        // When
        Set<ConstraintViolation<Produto>> violations = validator.validate(produto);
        
        // Then
        assertFalse(violations.isEmpty(), "Preço negativo deve gerar violações");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("valor")));
    }
}
