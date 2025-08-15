package com.example.demo.repository;

import com.example.demo.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Interface do repositório para a entidade Usuario.
 * Estende JpaRepository para obter métodos de CRUD (Create, Read, Update, Delete) prontos.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    /**
     * Busca um usuário pelo seu campo 'login'.
     * Este método é crucial para o Spring Security, pois é usado pelo 'UserDetailsService'
     * para carregar os dados do usuário durante o processo de autenticação.
     * O Spring Data JPA cria a implementação deste método automaticamente com base no nome.
     * * @param login O login do usuário a ser procurado.
     * @return Um objeto UserDetails (a própria entidade Usuario) se encontrado, ou null caso contrário.
     */
    UserDetails findByLogin(String login);
}