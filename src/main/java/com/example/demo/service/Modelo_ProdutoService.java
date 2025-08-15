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

/**
 * Serviço para gerenciar as operações de negócio da entidade Produto.
 */
@Service
public class Modelo_ProdutoService {

    private final ProdutoRepository produtoRepository;

    /**
     * Construtor com injeção de dependência. É a forma recomendada pelo Spring
     * para injetar beans, promovendo um acoplamento mais fraco e facilitando testes.
     * @param produtoRepository O repositório de dados para a entidade Produto.
     */
    @Autowired
    public Modelo_ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    /**
     * Busca todos os produtos, permitindo filtragem por nome e ordenação.
     * @param name Filtro opcional pelo nome do produto (case-insensitive).
     * @param sort Critério de ordenação opcional (ex: "preco,asc").
     * @return Uma lista de DTOs de resposta dos produtos encontrados.
     */
    @Transactional(readOnly = true) // Otimização: indica ao JPA que esta transação não fará alterações no banco.
    public List<ProdutoResponseDTO> findAll(String name, String sort) {
        // Lógica para criar o objeto de ordenação a partir do parâmetro 'sort'.
        Sort sortOrder = Sort.by("Nome").ascending(); // Define uma ordenação padrão por nome ascendente.
        if (sort != null) {
            if (sort.equalsIgnoreCase("preco,asc")) {
                sortOrder = Sort.by("Preco").ascending();
            } else if (sort.equalsIgnoreCase("preco,desc")) {
                sortOrder = Sort.by("Preco").descending();
            }
        }

        List<Modelo_Produto> produtos;
        if (name != null && !name.trim().isEmpty()) {
            // Se um nome for fornecido, usa o método do repositório para buscar por nome (contendo, ignorando maiúsculas/minúsculas).
            produtos = produtoRepository.findByNomeContainingIgnoreCase(name, sortOrder);
        } else {
            // Caso contrário, busca todos os produtos com a ordenação definida.
            produtos = produtoRepository.findAll(sortOrder);
        }

        // Converte a lista de entidades (Modelo_Produto) para uma lista de DTOs de resposta (ProdutoResponseDTO) usando Stream API.
        return produtos.stream()
                .map(ProdutoResponseDTO::new) // Para cada produto na lista, cria um novo ProdutoResponseDTO.
                .collect(Collectors.toList()); // Coleta os resultados em uma nova lista.
    }

    /**
     * Busca um produto específico pelo seu ID.
     * @param id O ID do produto a ser buscado.
     * @return O DTO de resposta do produto encontrado.
     * @throws ResourceNotFoundException se nenhum produto for encontrado com o ID fornecido.
     */
    @Transactional(readOnly = true)
    public ProdutoResponseDTO findById(Long id) {
        Modelo_Produto produto = produtoRepository.findById(id)
                // Se o produto não for encontrado, lança uma exceção personalizada.
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + id));
        return new ProdutoResponseDTO(produto); // Converte a entidade para DTO de resposta.
    }

    /**
     * Salva um novo produto no banco de dados.
     * @param produtoDTO O DTO de requisição contendo os dados do novo produto.
     * @return O DTO de resposta do produto que foi salvo.
     */
    @Transactional
    public ProdutoResponseDTO save(ProdutoRequestDTO produtoDTO) {
        // Converte o DTO de requisição para a entidade Modelo_Produto.
        Modelo_Produto produto = new Modelo_Produto();
        produto.setNome(produtoDTO.nome());
        produto.setDescricao(produtoDTO.descricao());
        produto.setPreco(produtoDTO.preco());
        produto.setQuantidade_estoque(produtoDTO.quantidadeEstoque());
        produto.setData_criacao(LocalDateTime.now()); // A data de criação é gerenciada pelo servidor.

        // Salva a entidade no banco de dados.
        Modelo_Produto produtoSalvo = produtoRepository.save(produto);
        return new ProdutoResponseDTO(produtoSalvo); // Retorna o DTO correspondente à entidade salva.
    }

    /**
     * Atualiza um produto existente.
     * @param id O ID do produto a ser atualizado.
     * @param produtoDetailsDTO O DTO com os novos dados do produto. Campos nulos são ignorados.
     * @return O DTO de resposta do produto atualizado.
     * @throws ResourceNotFoundException se o produto não for encontrado.
     */
    @Transactional
    public ProdutoResponseDTO update(Long id, ProdutoRequestDTO produtoDetailsDTO) {
        // Busca a entidade existente no banco de dados.
        Modelo_Produto existingProduto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado para atualização com o ID: " + id));

        // Lógica de atualização parcial (PATCH): só atualiza os campos que não forem nulos no DTO.
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

        // Salva as alterações no banco de dados.
        Modelo_Produto produtoAtualizado = produtoRepository.save(existingProduto);
        return new ProdutoResponseDTO(produtoAtualizado);
    }

    /**
     * Deleta um produto pelo seu ID.
     * @param id O ID do produto a ser deletado.
     * @throws ResourceNotFoundException se o produto não for encontrado.
     */
    @Transactional
    public void deleteById(Long id) {
        // Verifica se o produto existe antes de tentar deletar para fornecer uma mensagem de erro clara.
        if (!produtoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produto não encontrado para remoção com o ID: " + id);
        }
        produtoRepository.deleteById(id);
    }
}