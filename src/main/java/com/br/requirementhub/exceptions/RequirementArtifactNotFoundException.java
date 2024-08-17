package com.br.requirementhub.exceptions;

public class RequirementArtifactNotFoundException extends RuntimeException {
    public RequirementArtifactNotFoundException(String message) {
        super(message);
    }
}