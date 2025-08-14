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

// Imports para as validações
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Modelo_Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do produto é obrigatório.") 
    @Column(nullable = false)
    private String Nome;

    @Column(length = 1000)
    private String Descricao;

    @Positive(message = "O preço deve ser maior que zero.") 
    @Column(nullable = false)
    private Double Preco;

    @Positive(message = "A quantidade em estoque deve ser um número positivo.")
    @Column(name = "stock_quantity")
    private Integer Quantidade_estoque;

    @Column(name = "creation_date", updatable = false)
    private LocalDateTime Data_criacao;

}