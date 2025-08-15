package com.example.demo.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.demo.models.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Serviço responsável pela geração e validação de Tokens JWT.
 * Utiliza a biblioteca auth0-jwt.
 */
@Service
public class TokenService {

    // Injeta o segredo usado para assinar os tokens a partir do arquivo de configuração (application.properties ou application.yml).
    // É uma boa prática para não deixar segredos hardcoded no código.
    @Value("${api.security.token.secret}")
    private String secret;

    /**
     * Gera um novo token JWT para um usuário autenticado.
     * @param usuario O objeto do usuário para o qual o token será gerado.
     * @return Uma string contendo o token JWT.
     * @throws RuntimeException se ocorrer um erro durante a criação do token.
     */
    public String gerarToken(Usuario usuario) {
        try {
            // Define o algoritmo de assinatura do token, HMAC256, usando o segredo da aplicação.
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            
            // Inicia a criação do token.
            return JWT.create()
                    .withIssuer("API Produtos") // Define o emissor (quem está gerando o token).
                    .withSubject(usuario.getLogin()) // Define o "dono" do token, geralmente o ID ou login do usuário.
                    .withExpiresAt(dataExpiracao()) // Define a data e hora de expiração do token.
                    .sign(algoritmo); // Assina o token com o algoritmo definido.
        } catch (JWTCreationException exception){
            // Lança uma exceção de runtime se a criação do token falhar.
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    /**
     * Valida um token JWT e extrai o "subject" (neste caso, o login do usuário).
     * @param tokenJWT O token JWT a ser validado.
     * @return O login do usuário (subject) se o token for válido.
     * @throws RuntimeException se o token for inválido, expirado ou malformado.
     */
    public String getSubject(String tokenJWT) {
        try {
            // Define o mesmo algoritmo usado para a criação para poder verificar a assinatura.
            Algorithm algoritmo = Algorithm.HMAC256(secret);

            // Cria um objeto verificador configurado com as mesmas regras da criação (algoritmo e issuer).
            return JWT.require(algoritmo)
                    .withIssuer("API Produtos") // Verifica se o emissor do token é o mesmo esperado.
                    .build()
                    .verify(tokenJWT) // Tenta verificar o token. Lança uma exceção se a assinatura, expiração ou issuer estiverem incorretos.
                    .getSubject(); // Se a verificação for bem-sucedida, extrai e retorna o subject.
        } catch (JWTVerificationException exception) {
            // Lança uma exceção genérica para não dar detalhes sobre o erro da validação (boa prática de segurança).
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

    /**
     * Calcula o momento exato da expiração do token.
     * @return Um objeto Instant representando o ponto no tempo em que o token irá expirar.
     */
    private Instant dataExpiracao() {
        // Define que o token será válido por 2 horas a partir do momento atual.
        // O fuso horário "-03:00" (horário de Brasília) é usado para garantir consistência.
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}