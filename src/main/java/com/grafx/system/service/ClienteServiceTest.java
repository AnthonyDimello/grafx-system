package com.grafx.system.service;

import com.grafx.system.model.Cliente;
import com.grafx.system.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {
    
    @Mock
    private ClienteRepository clienteRepository;
    
    @InjectMocks
    private ClienteService clienteService;
    
    @Test
    void testFindAll() {
        // Given
        Cliente cliente1 = new Cliente("João", "11-9999-8888", "123.456.789-00", "joao@email.com", "Rua A");
        Cliente cliente2 = new Cliente("Maria", "11-7777-6666", "987.654.321-00", "maria@email.com", "Rua B");
        List<Cliente> expectedClientes = Arrays.asList(cliente1, cliente2);
        
        when(clienteRepository.findAll()).thenReturn(expectedClientes);
        
        // When
        List<Cliente> actualClientes = clienteService.findAll();
        
        // Then
        assertEquals(2, actualClientes.size());
        verify(clienteRepository, times(1)).findAll();
    }
    
    @Test
    void testFindById() {
        // Given
        Long id = 1L;
        Cliente expectedCliente = new Cliente("João", "11-9999-8888", "123.456.789-00", "joao@email.com", "Rua A");
        
        when(clienteRepository.findById(id)).thenReturn(Optional.of(expectedCliente));
        
        // When
        Optional<Cliente> actualCliente = clienteService.findById(id);
        
        // Then
        assertTrue(actualCliente.isPresent());
        assertEquals("João", actualCliente.get().getNome());
        verify(clienteRepository, times(1)).findById(id);
    }
    
    @Test
    void testSave() {
        // Given
        Cliente cliente = new Cliente("João", "11-9999-8888", "123.456.789-00", "joao@email.com", "Rua A");
        
        when(clienteRepository.save(cliente)).thenReturn(cliente);
        
        // When
        Cliente savedCliente = clienteService.save(cliente);
        
        // Then
        assertNotNull(savedCliente);
        assertEquals("João", savedCliente.getNome());
        verify(clienteRepository, times(1)).save(cliente);
    }
}
