package com.example.exception;

public class CompteIntrouvableException extends RuntimeException {

    public CompteIntrouvableException(String message) {
        super(message);
    }
}
