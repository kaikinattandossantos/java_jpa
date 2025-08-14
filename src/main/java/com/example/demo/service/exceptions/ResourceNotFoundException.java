package com.example.demo.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção customizada para ser lançada quando um recurso não é encontrado no banco de dados.
 * A anotação @ResponseStatus(HttpStatus.NOT_FOUND) faz com que o Spring retorne
 * automaticamente o código de status HTTP 404 quando esta exceção é lançada pelo controller.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}