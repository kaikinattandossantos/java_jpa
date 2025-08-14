package com.example.demo.controller;

import com.example.demo.models.Modelo_Produto;
import com.example.demo.service.Modelo_ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos") // Define o caminho base para todos os endpoints neste controller
public class ProdutoController {

    private final Modelo_ProdutoService produtoService;

    @Autowired
    public ProdutoController(Modelo_ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    /**
     * Endpoint para criar um novo produto.
     * HTTP Method: POST
     * URL: /produtos
     */
    @PostMapping
    public ResponseEntity<Modelo_Produto> criarProduto(@RequestBody Modelo_Produto produto) {
        Modelo_Produto novoProduto = produtoService.save(produto);
        return new ResponseEntity<>(novoProduto, HttpStatus.CREATED); // Retorna 201 Created
    }

    /**
     * Endpoint para listar todos os produtos.
     * HTTP Method: GET
     * URL: /produtos
     */
    @GetMapping
    public ResponseEntity<List<Modelo_Produto>> listarTodosProdutos() {
        List<Modelo_Produto> produtos = produtoService.findAll();
        return ResponseEntity.ok(produtos); // Retorna 200 OK
    }

    /**
     * Endpoint para obter os detalhes de um produto por seu ID.
     * HTTP Method: GET
     * URL: /produtos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Modelo_Produto> obterProdutoPorId(@PathVariable Long id) {
        Modelo_Produto produto = produtoService.findById(id);
        return ResponseEntity.ok(produto); // Retorna 200 OK
    }

    /**
     * Endpoint para atualizar um produto existente por seu ID.
     * HTTP Method: PUT
     * URL: /produtos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Modelo_Produto> atualizarProduto(@PathVariable Long id, @RequestBody Modelo_Produto produtoDetails) {
        Modelo_Produto produtoAtualizado = produtoService.update(id, produtoDetails);
        return ResponseEntity.ok(produtoAtualizado); // Retorna 200 OK
    }

    /**
     * Endpoint para remover um produto por seu ID.
     * HTTP Method: DELETE
     * URL: /produtos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerProduto(@PathVariable Long id) {
        produtoService.deleteById(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }
}