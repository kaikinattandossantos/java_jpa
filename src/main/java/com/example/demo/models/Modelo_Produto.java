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

import java.time.LocalDateTime; // Importação correta

@Getter
@Setter
@NoArgsConstructor // Adiciona um construtor sem argumentos (bom para JPA)
@AllArgsConstructor // Adiciona um construtor com todos os argumentos
@Entity
@Table(name = "products") // Define o nome da tabela no banco de dados
public class Modelo_Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // Garante que o nome não pode ser nulo
    private String Nome;

    @Column(length = 1000) // Define um tamanho máximo para a descrição
    private String Descricao; // Padrão camelCase: descricao -> description

    @Column(nullable = false)
    private Double Preco; // Padrão camelCase: preco -> price

    @Column(name = "stock_quantity") // Padrão snake_case no banco e camelCase no Java
    private Integer Quantidade_estoque; // Padrão camelCase: quantidade_estoque -> stockQuantity

    @Column(name = "creation_date", updatable = false) // Não pode ser atualizado após a criação
    private LocalDateTime Data_criacao; // Tipo correto e padrão camelCase

}