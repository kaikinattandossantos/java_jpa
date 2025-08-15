package com.example.demo.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de segurança que é executado uma vez para cada requisição.
 * Responsável por validar o token JWT e autenticar o usuário no contexto do Spring Security.
 */
@Component // Marca esta classe como um componente gerenciado pelo Spring.
public class SecurityFilter extends OncePerRequestFilter { // Garante que o filtro seja executado apenas uma vez por requisição.

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Lógica principal do filtro. Executada para cada requisição que passa por ele.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Tenta recuperar o token JWT do cabeçalho da requisição.
        var tokenJWT = recuperarToken(request);

        // 2. Se um token foi encontrado, prossiga com a validação.
        if (tokenJWT != null) {
            try {
                // 2a. Valida o token e extrai o "subject" (login do usuário).
                var subject = tokenService.getSubject(tokenJWT);
                // 2b. Com o login, busca o usuário completo no banco de dados.
                var usuario = usuarioRepository.findByLogin(subject);

                // 2c. Se o usuário existir, cria um objeto de autenticação.
                if (usuario != null) {
                    var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                    
                    // 2d. Define o usuário como autenticado no contexto de segurança do Spring.
                    // A partir daqui, o Spring sabe que a requisição é válida e quem é o usuário.
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JWTVerificationException exception){
                // Se o token for inválido (expirado, assinatura incorreta, etc.), uma exceção é lançada.
                // O bloco catch a captura, mas não faz nada. Isso significa que a autenticação falhou
                // e o SecurityContextHolder permanecerá vazio. O Spring então negará o acesso (retornando 403 Forbidden).
            }
        }
        
        // 3. Continua a execução da cadeia de filtros.
        // Se a autenticação foi bem-sucedida, o próximo filtro verá o usuário autenticado.
        // Se não, a requisição continuará sem autenticação e será barrada mais tarde pelas regras de autorização.
        filterChain.doFilter(request, response);
    }

    /**
     * Método auxiliar para extrair o token do cabeçalho "Authorization".
     * @param request a requisição HTTP.
     * @return o token JWT como uma String, ou null se não for encontrado.
     */
    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            // O token vem no formato "Bearer <token>". Esta linha remove o prefixo "Bearer ".
            return authorizationHeader.replace("Bearer ", "").trim();
        }
        return null; // Retorna nulo se o cabeçalho não existir.
    }
}