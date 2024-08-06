package com.br.requirementhub.exceptions;

public class RequirementNotFoundException extends RuntimeException {
    public RequirementNotFoundException(String message) {
        super(message);
    }
}