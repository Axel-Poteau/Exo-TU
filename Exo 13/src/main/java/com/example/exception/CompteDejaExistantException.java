package com.example.exception;

public class CompteDejaExistantException extends RuntimeException {

    public CompteDejaExistantException(String message) {
        super(message);
    }
}
