package com.br.requirementhub.exceptions;

public class RequirementArtifactAlreadyExistException extends RuntimeException {
    public RequirementArtifactAlreadyExistException(String message) {
        super(message);
    }
}