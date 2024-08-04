package com.br.requirementhub.exceptions;

public class ArtifactNotFoundException extends RuntimeException {
    public ArtifactNotFoundException(String message) {
        super(message);
    }
}