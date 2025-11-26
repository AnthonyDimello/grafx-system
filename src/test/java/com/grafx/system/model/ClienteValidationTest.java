package com.grafx.system.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ClienteValidationTest {
    
    private Validator validator;
    
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    @Test
    @DisplayName("CT005 - Validação de Cliente Válido")
    void testClienteValido() {
        // Given
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");
        cliente.setCpf("123.456.789-00");
        cliente.setTelefone("11-9999-8888");
        cliente.setEmail("joao@email.com");
        cliente.setEndereco("Rua Teste, 123");
        
        // When
        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);
        
        // Then
        assertTrue(violations.isEmpty(), "Cliente válido não deve ter violações");
    }
    
    @Test
    @DisplayName("CT006 - Validação de CPF Inválido (curto)")
    void testCpfInvalidoCurto() {
        // Given
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");
        cliente.setCpf("123"); // CPF inválido
        cliente.setTelefone("11-9999-8888");
        cliente.setEmail("joao@email.com");
        cliente.setEndereco("Rua Teste, 123");
        
        // When
        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);
        
        // Then
        assertFalse(violations.isEmpty(), "CPF inválido deve gerar violações");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("cpf")));
    }
    
    @Test
    @DisplayName("CT007 - Validação de Cliente Sem Nome")
    void testClienteSemNome() {
        // Given
        Cliente cliente = new Cliente();
        cliente.setNome(""); // Nome vazio
        cliente.setCpf("123.456.789-00");
        cliente.setTelefone("11-9999-8888");
        cliente.setEmail("joao@email.com");
        cliente.setEndereco("Rua Teste, 123");
        
        // When
        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);
        
        // Then
        assertFalse(violations.isEmpty(), "Cliente sem nome deve gerar violações");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
    }
    
    @Test
    @DisplayName("CT008 - Validação de Cliente Sem Telefone")
    void testClienteSemTelefone() {
        // Given
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");
        cliente.setCpf("123.456.789-00");
        cliente.setTelefone(""); // Telefone vazio
        cliente.setEmail("joao@email.com");
        cliente.setEndereco("Rua Teste, 123");
        
        // When
        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);
        
        // Then
        assertFalse(violations.isEmpty(), "Cliente sem telefone deve gerar violações");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("telefone")));
    }
    
    @Test
    @DisplayName("CT009 - Validação de CPF Nulo")
    void testCpfNulo() {
        // Given
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");
        cliente.setCpf(null); // CPF nulo
        cliente.setTelefone("11-9999-8888");
        cliente.setEmail("joao@email.com");
        cliente.setEndereco("Rua Teste, 123");
        
        // When
        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);
        
        // Then
        assertFalse(violations.isEmpty(), "CPF nulo deve gerar violações");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("cpf")));
    }
}
