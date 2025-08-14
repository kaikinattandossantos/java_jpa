package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

// DTO para receber dados nas requisições POST e PUT.
// Contém apenas os campos que o cliente pode fornecer e as validações.
public record ProdutoRequestDTO(
    @NotBlank(message = "O nome do produto é obrigatório.")
    @Size(max = 255)
    String nome,

    @Size(max = 1000)
    String descricao,

    @Positive(message = "O preço deve ser maior que zero.")
    Double preco,

    @Positive(message = "A quantidade em estoque deve ser um número positivo.")
    Integer quantidadeEstoque
) {}