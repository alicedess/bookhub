package fr.eni.bookhub.controller;

import fr.eni.bookhub.exception.OperationException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Gestion globale des exceptions.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(OperationException.class)
    public ResponseEntity<?> handleOperation(OperationException e) {
        return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception e, HttpServletRequest request) {
        log.error("Erreur sur {} {} : {}", request.getMethod(), request.getRequestURI(), e.getMessage(), e);

        return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Une erreur s'est produite"
        ));
    }
}
