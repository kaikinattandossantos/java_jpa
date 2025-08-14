package com.example.demo.repository;

import com.example.demo.models.Modelo_Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Modelo_Produto, Long> {
}