package com.example.demo.controller;

import com.example.demo.dto.ProdutoRequestDTO;
import com.example.demo.dto.ProdutoResponseDTO;
import com.example.demo.service.Modelo_ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Produtos", description = "Endpoints para o gerenciamento de produtos")
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final Modelo_ProdutoService produtoService;

    @Autowired
    public ProdutoController(Modelo_ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @Operation(summary = "Cria um novo produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoResponseDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> criarProduto(@Valid @RequestBody ProdutoRequestDTO produtoRequestDTO) {
        ProdutoResponseDTO novoProduto = produtoService.save(produtoRequestDTO);
        return new ResponseEntity<>(novoProduto, HttpStatus.CREATED);
    }
    
    @Operation(summary = "Busca um produto por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoResponseDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> obterProdutoPorId(@PathVariable Long id) {
        ProdutoResponseDTO produto = produtoService.findById(id);
        return ResponseEntity.ok(produto);
    }
    
    // ... adicione as anotações @Operation e @ApiResponses para os outros métodos também ...
    
    @Operation(summary = "Lista produtos com filtros e ordenação")
    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listarTodosProdutos(
            @Parameter(description = "Filtrar produtos pelo nome (parcial, case-insensitive)") @RequestParam(required = false) String name,
            @Parameter(description = "Ordenar por preço. Use 'preco,asc' ou 'preco,desc'") @RequestParam(required = false) String sort
    ) {
        List<ProdutoResponseDTO> produtos = produtoService.findAll(name, sort);
        return ResponseEntity.ok(produtos);
    }

    @Operation(summary = "Atualiza um produto existente")
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(@PathVariable Long id, @Valid @RequestBody ProdutoRequestDTO produtoDetailsDTO) {
        ProdutoResponseDTO produtoAtualizado = produtoService.update(id, produtoDetailsDTO);
        return ResponseEntity.ok(produtoAtualizado);
    }

    @Operation(summary = "Remove um produto por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto removido com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerProduto(@PathVariable Long id) {
        produtoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}