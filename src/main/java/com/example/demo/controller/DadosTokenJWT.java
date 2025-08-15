package com.example.demo.controller;

// Este record representa o JSON que a API retorna após um login bem-sucedido.
// Ex: { "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." }
public record DadosTokenJWT(String token) {}