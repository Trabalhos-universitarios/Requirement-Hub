package com.br.requirementhub.exceptions;

public class RequirementAlreadyExistException extends RuntimeException {
    public RequirementAlreadyExistException(String message) {
        super(message);
    }
}