package com.br.requirementhub.exceptions;

public class ProjectAlreadyExistException extends RuntimeException {
    public ProjectAlreadyExistException(String message) {
        super(message);
    }
}