package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Entidade JPA que representa um usuário no banco de dados.
 * Esta classe também implementa a interface UserDetails do Spring Security,
 * o que a torna a fonte de informações de usuário para fins de autenticação e autorização.
 */
@Entity // Marca a classe como uma entidade JPA, que será mapeada para uma tabela no banco de dados.
@Table(name = "usuarios") // Especifica o nome da tabela no banco de dados.
@Getter // Anotação do Lombok que gera automaticamente os métodos getters para todos os campos.
@NoArgsConstructor // Lombok: gera um construtor sem argumentos (requerido pelo JPA).
@AllArgsConstructor // Lombok: gera um construtor com todos os campos como argumentos.
@EqualsAndHashCode(of = "id") // Lombok: gera os métodos equals() e hashCode() baseados apenas no campo 'id'.
public class Usuario implements UserDetails {

    @Id // Marca o campo 'id' como a chave primária da tabela.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura a geração automática do ID pelo banco de dados (autoincremento).
    private Long id;

    private String login; // Armazena o login do usuário (username).
    private String senha; // Armazena a senha do usuário (password), que deve ser codificada (hashed).

    // --- Métodos da interface UserDetails ---
    // O Spring Security utiliza esses métodos para gerenciar a autenticação e autorização.

    /**
     * Retorna as permissões (autoridades/roles) concedidas ao usuário.
     * @return Uma coleção de GrantedAuthority.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Para um sistema simples, uma role fixa "ROLE_USER" é retornada para todos os usuários.
        // Em um sistema mais complexo, as roles seriam carregadas do banco de dados, associadas a este usuário.
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    /**
     * Retorna a senha usada para autenticar o usuário.
     * @return A senha codificada (hashed).
     */
    @Override
    public String getPassword() {
        return senha;
    }

    /**
     * Retorna o nome de usuário usado para autenticar.
     * @return O login do usuário.
     */
    @Override
    public String getUsername() {
        return login;
    }

    /**
     * Indica se a conta do usuário não expirou.
     */
    @Override
    public boolean isAccountNonExpired() { 
        // Para este exemplo, a conta nunca expira.
        return true; 
    }

    /**
     * Indica se o usuário não está bloqueado.
     */
    @Override
    public boolean isAccountNonLocked() { 
        // Para este exemplo, a conta nunca é bloqueada.
        return true; 
    }

    /**
     * Indica se as credenciais do usuário (senha) não expiraram.
     */
    @Override
    public boolean isCredentialsNonExpired() { 
        // Para este exemplo, as credenciais nunca expiram.
        return true; 
    }

    /**
     * Indica se o usuário está habilitado ou desabilitado.
     */
    @Override
    public boolean isEnabled() { 
        // Para este exemplo, o usuário está sempre habilitado.
        return true; 
    }
}