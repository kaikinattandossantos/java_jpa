package com.example.demo.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Getter
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    private String name;
    private String email;

    // Construtor vazio obrigatório para JPA
    public User() {
    }

    // Construtor com campos para facilitar criação
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }


}
