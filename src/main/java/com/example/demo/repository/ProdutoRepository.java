package com.example.demo.repository;

import com.example.demo.models.Modelo_Produto;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Modelo_Produto, Long> {

    // Novo método para buscar produtos cujo nome contém a string fornecida (ignorando maiúsculas/minúsculas)
    // e aplicando uma ordenação.
    List<Modelo_Produto> findByNomeContainingIgnoreCase(String nome, Sort sort);

}