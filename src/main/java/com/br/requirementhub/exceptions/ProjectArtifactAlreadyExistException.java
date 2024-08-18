package com.br.requirementhub.exceptions;

public class ProjectArtifactAlreadyExistException extends RuntimeException {
    public ProjectArtifactAlreadyExistException(String message) {
        super(message);
    }
}