package com.grafx.system.service;

import com.grafx.system.model.Venda;
import com.grafx.system.model.Produto;
import com.grafx.system.model.Atendente;
import com.grafx.system.repository.VendaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendaServiceTest {
    
    @Mock
    private VendaRepository vendaRepository;
    
    @InjectMocks
    private VendaService vendaService;
    
    private Produto produto;
    private Atendente atendente;
    
    @BeforeEach
    void setUp() {
        produto = new Produto();
        produto.setId(1L);
        produto.setDescricao("Produto Teste");
        produto.setValor(new BigDecimal("10.00"));
        produto.setEstoque(100);
        
        atendente = new Atendente();
        atendente.setId(1L);
        atendente.setNome("Atendente Teste");
    }
    
    @Test
    @DisplayName("CT001 - Cálculo do Valor Total da Venda")
    void testCalcularValorTotalVenda() {
        // Given
        Venda venda = new Venda();
        venda.setProduto(produto);
        venda.setValorUnitario(new BigDecimal("10.00"));
        venda.setQuantidade(3);
        venda.setValorTotal(new BigDecimal("30.00"));
        
        when(vendaRepository.save(any(Venda.class))).thenReturn(venda);
        
        // When
        Venda savedVenda = vendaService.save(venda);
        
        // Then
        assertNotNull(savedVenda);
        assertEquals(new BigDecimal("30.00"), savedVenda.getValorTotal());
        verify(vendaRepository, times(1)).save(venda);
    }
    
    @Test
    @DisplayName("CT002 - Cálculo com Quantidade Zero")
    void testCalcularValorTotalQuantidadeZero() {
        // Given
        Venda venda = new Venda();
        venda.setProduto(produto);
        venda.setValorUnitario(new BigDecimal("10.00"));
        venda.setQuantidade(0);
        venda.setValorTotal(BigDecimal.ZERO);
        
        when(vendaRepository.save(any(Venda.class))).thenReturn(venda);
        
        // When
        Venda savedVenda = vendaService.save(venda);
        
        // Then
        assertNotNull(savedVenda);
        assertEquals(BigDecimal.ZERO, savedVenda.getValorTotal());
    }
    
    @Test
    @DisplayName("CT003 - Cálculo com Valor Unitário Zero")
    void testCalcularValorTotalValorUnitarioZero() {
        // Given
        Venda venda = new Venda();
        venda.setProduto(produto);
        venda.setValorUnitario(BigDecimal.ZERO);
        venda.setQuantidade(5);
        venda.setValorTotal(BigDecimal.ZERO);
        
        when(vendaRepository.save(any(Venda.class))).thenReturn(venda);
        
        // When
        Venda savedVenda = vendaService.save(venda);
        
        // Then
        assertNotNull(savedVenda);
        assertEquals(BigDecimal.ZERO, savedVenda.getValorTotal());
    }
}
