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

    @ExceptionHandler(CompteIntrouvableException.class)
    public ResponseEntity<Map<String, String>> gererIntrouvable(CompteIntrouvableException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(CompteDejaExistantException.class)
    public ResponseEntity<Map<String, String>> gererDejaExistant(CompteDejaExistantException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(SoldeInsuffisantException.class)
    public ResponseEntity<Map<String, String>> gererSoldeInsuffisant(SoldeInsuffisantException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
    }
}
