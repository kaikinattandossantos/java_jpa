package com.example.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Imports para as validações do Bean Validation
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

/**
 * Entidade JPA que representa um produto na tabela 'products'.
 * Inclui anotações de validação para garantir a integridade dos dados.
 */
@Getter // Lombok: gera getters para todos os campos.
@Setter // Lombok: gera setters para todos os campos.
@NoArgsConstructor // Lombok: gera um construtor padrão.
@AllArgsConstructor // Lombok: gera um construtor com todos os campos.
@Entity // Marca como uma entidade JPA.
@Table(name = "products") // Mapeia para a tabela 'products'.
public class Modelo_Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // A anotação @NotBlank garante que o nome não seja nulo nem contenha apenas espaços em branco.
    @NotBlank(message = "O nome do produto é obrigatório.")
    // @Column(nullable = false) impõe a restrição a nível de banco de dados.
    @Column(nullable = false)
    private String Nome; // Sugestão: usar camelCase (nome) para seguir a convenção Java.

    // Define o tamanho máximo da coluna de descrição no banco de dados.
    @Column(length = 1000)
    private String Descricao; // Sugestão: usar camelCase (descricao).

    // A anotação @Positive garante que o preço seja um número maior que zero.
    @Positive(message = "O preço deve ser maior que zero.")
    @Column(nullable = false)
    private Double Preco; // Sugestão: usar camelCase (preco).

    // Garante que a quantidade em estoque seja um número positivo.
    @Positive(message = "A quantidade em estoque deve ser um número positivo.")
    // Mapeia este campo para a coluna 'stock_quantity' no banco de dados.
    @Column(name = "stock_quantity")
    private Integer Quantidade_estoque; // Sugestão: usar camelCase (quantidadeEstoque).

    // Mapeia para a coluna 'creation_date'. 'updatable = false' impede que este campo seja alterado em um UPDATE.
    @Column(name = "creation_date", updatable = false)
    private LocalDateTime Data_criacao; // Sugestão: usar camelCase (dataCriacao).

}