package com.example.exception;

public class TicketIntrouvableException extends RuntimeException {

    public TicketIntrouvableException(String message) {
        super(message);
    }
}
