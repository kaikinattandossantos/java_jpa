package com.example.demo.service;

import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Serviço de autenticação que se integra com o Spring Security.
 * Implementa a interface UserDetailsService para carregar dados do usuário pelo login.
 */
@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired // Injeção de dependência por campo. Para classes simples como esta, é aceitável, mas a injeção por construtor é geralmente preferível.
    private UsuarioRepository repository;

    /**
     * Método principal do Spring Security para carregar um usuário pelo seu nome de usuário (neste caso, "login").
     * O Spring Security chama este método durante o processo de autenticação.
     *
     * @param username O login do usuário que está tentando se autenticar.
     * @return Um objeto UserDetails contendo as informações do usuário (login, senha, permissões).
     * @throws UsernameNotFoundException se o usuário com o login fornecido não for encontrado no banco de dados.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca o usuário no repositório pelo seu login.
        UserDetails user = repository.findByLogin(username);
        
        // Se o usuário não for encontrado, é crucial lançar UsernameNotFoundException.
        // O Spring Security captura esta exceção para saber que a autenticação falhou por "usuário não encontrado".
        if (user == null) {
            throw new UsernameNotFoundException("Dados de login inválidos.");
        }
        
        // Retorna o objeto UserDetails (a própria entidade Usuario, que deve implementar esta interface) para o Spring Security.
        return user;
    }
}