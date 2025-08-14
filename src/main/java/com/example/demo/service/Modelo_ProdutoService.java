package com.example.demo.service;

import com.example.demo.models.Modelo_Produto;
import com.example.demo.repository.ProdutoRepository; // Alterado
import com.example.demo.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Modelo_ProdutoService {

    private final ProdutoRepository produtoRepository; // Alterado

    @Autowired
    public Modelo_ProdutoService(ProdutoRepository produtoRepository) { // Alterado
        this.produtoRepository = produtoRepository;
    }

    public List<Modelo_Produto> findAll() {
        return produtoRepository.findAll();
    }

    public Modelo_Produto findById(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + id));
    }

    public Modelo_Produto save(Modelo_Produto produto) {
        return produtoRepository.save(produto);
    }

    public Modelo_Produto update(Long id, Modelo_Produto produtoDetails) {
        Modelo_Produto existingProduto = findById(id);

        existingProduto.setNome(produtoDetails.getNome());
        existingProduto.setDescricao(produtoDetails.getDescricao());
        existingProduto.setPreco(produtoDetails.getPreco());
        existingProduto.setQuantidade_estoque(produtoDetails.getQuantidade_estoque());

        return produtoRepository.save(existingProduto);
    }

    public void deleteById(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produto não encontrado com o ID: " + id);
        }
        produtoRepository.deleteById(id);
    }
}