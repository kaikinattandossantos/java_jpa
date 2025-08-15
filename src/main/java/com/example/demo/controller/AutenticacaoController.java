package com.example.demo.controller;

import com.example.demo.models.Usuario;
import com.example.demo.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    // AuthenticationManager é o componente central do Spring Security para processar a autenticação.
    @Autowired
    private AuthenticationManager manager;
    
    // Serviço para gerar o token JWT após a autenticação.
    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<DadosTokenJWT> efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        // 1. Cria um objeto 'token' de autenticação com as credenciais recebidas.
        // Este objeto ainda não está "autenticado".
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        
        // 2. Chama o AuthenticationManager. Este é o passo que dispara o processo de autenticação.
        // O Spring Security irá chamar o seu 'AutenticacaoService' (UserDetailsService) para buscar o usuário
        // e então comparar as senhas. Se falhar, uma exceção será lançada.
        var authentication = manager.authenticate(authenticationToken);

        // 3. Se a autenticação foi bem-sucedida, o objeto 'authentication' contém os dados do usuário autenticado.
        // Extraímos o usuário (Principal) e o passamos para o nosso serviço de token.
        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        // 4. Retorna o token JWT gerado para o cliente com o status 200 OK.
        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }
}