package com.example.demo.controller;

// Este record representa o JSON que o cliente envia no corpo da requisição de login.
// Ex: { "login": "user", "senha": "123" }
public record DadosAutenticacao(String login, String senha) {
}