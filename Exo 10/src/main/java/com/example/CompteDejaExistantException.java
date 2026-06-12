package com.example;

public class CompteDejaExistantException extends RuntimeException {

    public CompteDejaExistantException(String message) {
        super(message);
    }
}
