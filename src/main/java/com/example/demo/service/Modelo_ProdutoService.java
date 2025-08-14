package com.example.demo.service;

import com.example.demo.dto.ProdutoRequestDTO;
import com.example.demo.dto.ProdutoResponseDTO;
import com.example.demo.models.Modelo_Produto;
import com.example.demo.repository.ProdutoRepository;
import com.example.demo.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Modelo_ProdutoService {

    private final ProdutoRepository produtoRepository;

    @Autowired
    public Modelo_ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> findAll(String name, String sort) {
        // Lógica para criar o objeto de ordenação a partir da string
        Sort sortOrder = Sort.by("Nome").ascending(); // Ordenação padrão
        if (sort != null) {
            if (sort.equalsIgnoreCase("preco,asc")) {
                sortOrder = Sort.by("Preco").ascending();
            } else if (sort.equalsIgnoreCase("preco,desc")) {
                sortOrder = Sort.by("Preco").descending();
            }
        }

        List<Modelo_Produto> produtos;
        if (name != null && !name.trim().isEmpty()) {
            // Chama o novo método do repositório se o filtro de nome for fornecido
            produtos = produtoRepository.findByNomeContainingIgnoreCase(name, sortOrder);
        } else {
            // Caso contrário, busca todos com a ordenação definida
            produtos = produtoRepository.findAll(sortOrder);
        }

        // Mapeia a lista de entidades para uma lista de DTOs de resposta
        return produtos.stream()
                .map(ProdutoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProdutoResponseDTO findById(Long id) {
        Modelo_Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + id));
        return new ProdutoResponseDTO(produto); // Retorna o DTO
    }

    @Transactional
    public ProdutoResponseDTO save(ProdutoRequestDTO produtoDTO) {
        // Converte o DTO de requisição para uma Entidade
        Modelo_Produto produto = new Modelo_Produto();
        produto.setNome(produtoDTO.nome());
        produto.setDescricao(produtoDTO.descricao());
        produto.setPreco(produtoDTO.preco());
        produto.setQuantidade_estoque(produtoDTO.quantidadeEstoque());
        produto.setData_criacao(LocalDateTime.now()); // Define a data de criação no servidor

        Modelo_Produto produtoSalvo = produtoRepository.save(produto);
        return new ProdutoResponseDTO(produtoSalvo); // Retorna o DTO de resposta
    }

    @Transactional
    public ProdutoResponseDTO update(Long id, ProdutoRequestDTO produtoDetailsDTO) {
        // Busca a entidade existente no banco
        Modelo_Produto existingProduto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado para atualização com o ID: " + id));

        // Lógica de atualização inteligente: só atualiza campos não nulos do DTO
        if (produtoDetailsDTO.nome() != null) {
            existingProduto.setNome(produtoDetailsDTO.nome());
        }
        if (produtoDetailsDTO.descricao() != null) {
            existingProduto.setDescricao(produtoDetailsDTO.descricao());
        }
        if (produtoDetailsDTO.preco() != null) {
            existingProduto.setPreco(produtoDetailsDTO.preco());
        }
        if (produtoDetailsDTO.quantidadeEstoque() != null) {
            existingProduto.setQuantidade_estoque(produtoDetailsDTO.quantidadeEstoque());
        }

        Modelo_Produto produtoAtualizado = produtoRepository.save(existingProduto);
        return new ProdutoResponseDTO(produtoAtualizado);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produto não encontrado para remoção com o ID: " + id);
        }
        produtoRepository.deleteById(id);
    }
}