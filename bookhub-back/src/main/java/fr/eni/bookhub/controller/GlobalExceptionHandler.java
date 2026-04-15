package fr.eni.bookhub.controller;

import fr.eni.bookhub.exception.OperationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Gestion globale des exceptions.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(OperationException.class)
    public ResponseEntity<?> handleOperation(OperationException e) {
        return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception e) {
        return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Une erreur s'est produite"
        ));
    }
}
