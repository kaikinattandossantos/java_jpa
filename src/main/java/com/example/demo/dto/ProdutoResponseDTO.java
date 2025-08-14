package com.example.demo.dto;

import com.example.demo.models.Modelo_Produto;
import java.time.LocalDateTime;

// DTO para enviar dados nas respostas da API.
// Contém apenas os campos que o cliente deve ver.
public record ProdutoResponseDTO(
    Long id,
    String nome,
    String descricao,
    Double preco,
    Integer quantidadeEstoque,
    LocalDateTime dataCriacao
) {
    // Construtor auxiliar para facilitar a conversão da Entidade para DTO
    public ProdutoResponseDTO(Modelo_Produto produto) {
        this(
            produto.getId(),
            produto.getNome(),
            produto.getDescricao(),
            produto.getPreco(),
            produto.getQuantidade_estoque(),
            produto.getData_criacao()
        );
    }
}