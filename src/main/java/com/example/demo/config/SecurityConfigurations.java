package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Classe de configuração central para o Spring Security.
 * Define a cadeia de filtros de segurança, o gerenciador de autenticação e o codificador de senhas.
 */
@Configuration // Marca a classe como uma fonte de definições de beans para o contexto da aplicação.
@EnableWebSecurity // Habilita o suporte à segurança web do Spring Security.
public class SecurityConfigurations {
    
    // Injeta nosso filtro personalizado que validará o token JWT.
    @Autowired
    private SecurityFilter securityFilter;

    /**
     * Define a cadeia de filtros de segurança que será aplicada a todas as requisições HTTP.
     * @param http o objeto HttpSecurity para configurar a segurança.
     * @return a cadeia de filtros de segurança construída.
     * @throws Exception se ocorrer um erro na configuração.
     */
    @Bean // Expõe o SecurityFilterChain como um Bean gerenciado pelo Spring.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            // 1. Desabilita a proteção CSRF (Cross-Site Request Forgery).
            // Como a API é stateless (sem sessão) e usa tokens, essa proteção não é necessária.
            .csrf(csrf -> csrf.disable())
            
            // 2. Configura o gerenciamento de sessão para ser STATELESS.
            // Isso informa ao Spring para não criar ou usar sessões HTTP. Cada requisição deve ser autossuficiente (com o token).
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 3. Configura as regras de autorização para as requisições HTTP.
            .authorizeHttpRequests(req -> {
                // Permite acesso público (sem autenticação) ao endpoint de login.
                req.requestMatchers(HttpMethod.POST, "/login").permitAll();
                
                // Permite acesso público aos endpoints da documentação do Swagger/OpenAPI.
                req.requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll(); 
                
                // Exige autenticação para todas as outras requisições.
                req.anyRequest().authenticated();
            })
            
            // 4. Adiciona nosso filtro personalizado (SecurityFilter) antes do filtro padrão do Spring.
            // Isso garante que nosso filtro de token seja executado primeiro para validar o JWT.
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
            
            .build(); // Constrói o SecurityFilterChain.
    }

    /**
     * Expõe o AuthenticationManager como um Bean para ser usado em outras partes da aplicação,
     * como no AutenticacaoController para processar o login.
     * @param configuration a configuração de autenticação do Spring.
     * @return o AuthenticationManager.
     * @throws Exception se ocorrer um erro ao obter o manager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Define o algoritmo de codificação de senhas a ser usado na aplicação.
     * BCrypt é o padrão recomendado por ser forte e seguro.
     * @return uma instância do PasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}