package com.example.demo.controller;

import com.example.demo.dto.ProdutoRequestDTO;
import com.example.demo.dto.ProdutoResponseDTO;
import com.example.demo.service.Modelo_ProdutoService;
// Imports do Swagger/OpenAPI para documentação da API
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

// @Tag agrupa todos os endpoints deste controller sob o nome "Produtos" na UI do Swagger.
@Tag(name = "Produtos", description = "Endpoints para o gerenciamento de produtos")
// @RestController combina @Controller e @ResponseBody, simplificando a criação de APIs REST.
@RestController
// @RequestMapping define o caminho base para todos os endpoints deste controller.
@RequestMapping("/produtos")
public class ProdutoController {

    // A injeção de dependência é feita na camada de serviço.
    private final Modelo_ProdutoService produtoService;

    // Injeção de dependência via construtor: uma boa prática que torna as dependências explícitas.
    @Autowired
    public ProdutoController(Modelo_ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // @Operation e @ApiResponses documentam o endpoint na especificação OpenAPI (Swagger).
    @Operation(summary = "Cria um novo produto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Produto criado com sucesso",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProdutoResponseDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content)
    })
    @PostMapping // Mapeia requisições HTTP POST para este método.
    public ResponseEntity<ProdutoResponseDTO> criarProduto(@Valid @RequestBody ProdutoRequestDTO produtoRequestDTO) {
        // @Valid ativa as validações (ex: @NotBlank) definidas no DTO.
        // @RequestBody converte o JSON do corpo da requisição para o objeto DTO.
        ProdutoResponseDTO novoProduto = produtoService.save(produtoRequestDTO);
        // Retorna o novo produto com o status HTTP 201 Created.
        return new ResponseEntity<>(novoProduto, HttpStatus.CREATED);
    }
    
    @Operation(summary = "Busca um produto por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto encontrado",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = ProdutoResponseDTO.class)) }),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content)
    })
    @GetMapping("/{id}") // Mapeia requisições GET para /produtos/{id}.
    public ResponseEntity<ProdutoResponseDTO> obterProdutoPorId(@PathVariable Long id) {
        // @PathVariable extrai o valor do {id} da URL.
        ProdutoResponseDTO produto = produtoService.findById(id);
        // Retorna o produto encontrado com o status HTTP 200 OK.
        return ResponseEntity.ok(produto);
    }
    
    @Operation(summary = "Lista produtos com filtros e ordenação")
    @GetMapping // Mapeia requisições GET para /produtos.
    public ResponseEntity<List<ProdutoResponseDTO>> listarTodosProdutos(
        // @Parameter documenta os parâmetros de consulta na UI do Swagger.
        @Parameter(description = "Filtrar produtos pelo nome (parcial, case-insensitive)") 
        @RequestParam(required = false) String name, // @RequestParam extrai parâmetros da URL (?name=...).
        
        @Parameter(description = "Ordenar por preço. Use 'preco,asc' ou 'preco,desc'") 
        @RequestParam(required = false) String sort
    ) {
        List<ProdutoResponseDTO> produtos = produtoService.findAll(name, sort);
        return ResponseEntity.ok(produtos);
    }

    @Operation(summary = "Atualiza um produto existente")
    @PutMapping("/{id}") // Mapeia requisições HTTP PUT.
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(@PathVariable Long id, @Valid @RequestBody ProdutoRequestDTO produtoDetailsDTO) {
        ProdutoResponseDTO produtoAtualizado = produtoService.update(id, produtoDetailsDTO);
        return ResponseEntity.ok(produtoAtualizado);
    }

    @Operation(summary = "Remove um produto por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Produto removido com sucesso", content = @Content),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content)
    })
    @DeleteMapping("/{id}") // Mapeia requisições HTTP DELETE.
    public ResponseEntity<Void> removerProduto(@PathVariable Long id) {
        produtoService.deleteById(id);
        // Retorna uma resposta vazia com status HTTP 204 No Content, indicando sucesso na remoção.
        return ResponseEntity.noContent().build();
    }
}