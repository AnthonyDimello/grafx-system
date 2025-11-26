package com.grafx.system.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CalculoFinanceiroTest {
    
    @Test
    @DisplayName("CT015 - Cálculo do Total da Venda")
    void testCalculoTotalVenda() {
        // Given
        int quantidade = 3;
        BigDecimal precoUnitario = new BigDecimal("10.00");
        
        // When
        BigDecimal total = precoUnitario.multiply(new BigDecimal(quantidade));
        
        // Then
        assertEquals(new BigDecimal("30.00"), total);
    }
    
    @Test
@DisplayName("CT016 - Cálculo com Quantidade Zero - CORRIGIDO")
void testCalculoQuantidadeZero() {
    // Given
    int quantidade = 0;
    BigDecimal precoUnitario = new BigDecimal("10.00");
    
    // When
    BigDecimal total = precoUnitario.multiply(new BigDecimal(quantidade));
    
    // Then - COMPARAÇÃO CORRIGIDA
    assertEquals(0, total.compareTo(BigDecimal.ZERO)); // ← CORREÇÃO
    // OU
    assertTrue(total.compareTo(BigDecimal.ZERO) == 0); // ← ALTERNATIVA
}
    
    @Test
    @DisplayName("CT017 - Cálculo com Preço Unitário Zero")
    void testCalculoPrecoUnitarioZero() {
        // Given
        int quantidade = 5;
        BigDecimal precoUnitario = BigDecimal.ZERO;
        
        // When
        BigDecimal total = precoUnitario.multiply(new BigDecimal(quantidade));
        
        // Then
        assertEquals(BigDecimal.ZERO, total);
    }
    
    @Test
    @DisplayName("CT004 - Cálculo com Valores Negativos")
    void testCalculoValoresNegativos() {
        // Given
        int quantidade = -2;
        BigDecimal precoUnitario = new BigDecimal("-5.00");
        
        // When
        BigDecimal total = precoUnitario.multiply(new BigDecimal(quantidade));
        
        // Then
        assertEquals(new BigDecimal("10.00"), total);
    }
}
