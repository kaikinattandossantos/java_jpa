package com.example.demo.repository;

import com.example.demo.models.Modelo_Produto;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface do repositório para a entidade Modelo_Produto.
 * Fornece operações de CRUD e consultas personalizadas para produtos.
 */
@Repository // Anotação opcional, mas boa prática para indicar que é um bean de acesso a dados.
public interface ProdutoRepository extends JpaRepository<Modelo_Produto, Long> {

    /**
     * Busca uma lista de produtos cujo nome contém uma determinada string, ignorando
     * a diferença entre maiúsculas e minúsculas, e aplica uma ordenação.
     * * A consulta SQL correspondente é gerada automaticamente pelo Spring Data JPA
     * com base no nome do método.
     * - findBy...: Inicia a consulta.
     * - Nome: Filtra pelo campo 'Nome' da entidade.
     * - Containing: Usa a lógica do 'LIKE %string%'.
     * - IgnoreCase: Converte os valores para minúsculo antes de comparar (ex: LOWER(nome)).
     * * @param nome A string a ser buscada no nome dos produtos.
     * @param sort Um objeto Sort que define o critério de ordenação.
     * @return Uma lista de entidades Modelo_Produto que correspondem ao critério.
     */
    List<Modelo_Produto> findByNomeContainingIgnoreCase(String nome, Sort sort);

}