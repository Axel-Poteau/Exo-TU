package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GestionnaireExceptions {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> gererValidation(ValidationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(TicketIntrouvableException.class)
    public ResponseEntity<Map<String, String>> gererIntrouvable(TicketIntrouvableException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(TransitionInterditeException.class)
    public ResponseEntity<Map<String, String>> gererTransitionInterdite(TransitionInterditeException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
    }
}
