package com.br.requirementhub.exceptions;

import org.springframework.web.server.ResponseStatusException;

public class RequirementAlreadyExistException extends RuntimeException {
    public RequirementAlreadyExistException(String message) {
        super(message);
    }
}