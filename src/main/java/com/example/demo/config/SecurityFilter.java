package com.example.demo.config;

import com.auth0.jwt.exceptions.JWTVerificationException; // Importe exceções relevantes
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

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            try {
                var subject = tokenService.getSubject(tokenJWT);
                var usuario = usuarioRepository.findByLogin(subject);

                // Adiciona uma verificação para o caso do usuário não ser encontrado
                if (usuario != null) {
                    var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JWTVerificationException exception){
                // Se o token for inválido (expirado, assinatura incorreta, etc.),
                // a exceção é capturada, e nada acontece. O Spring entenderá que a
                // requisição não está autenticada e retornará 403 Forbidden.
                // Você pode adicionar um log aqui, se quiser.
                // ex: logger.error("Erro na validação do Token JWT!", exception);
            }
        }
        
        // Independentemente do que acontecer, continua o fluxo da requisição.
        // Se o SecurityContext não foi populado, o Spring cuidará de barrar o acesso.
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            // Garante que o replace não vai quebrar se o cabeçalho for apenas "Bearer"
            return authorizationHeader.replace("Bearer ", "").trim();
        }
        return null;
    }
}