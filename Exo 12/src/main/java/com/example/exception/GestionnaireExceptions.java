package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

// Transforme les exceptions metier en reponses HTTP coherentes
@RestControllerAdvice
public class GestionnaireExceptions {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> gererValidation(ValidationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(RessourceIntrouvableException.class)
    public ResponseEntity<Map<String, String>> gererIntrouvable(RessourceIntrouvableException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(ConflitReservationException.class)
    public ResponseEntity<Map<String, String>> gererConflit(ConflitReservationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
    }
}
