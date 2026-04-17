package fr.eni.bookhub.controller;

import fr.eni.bookhub.dto.error.ApiErrorResponse;
import fr.eni.bookhub.dto.error.FieldErrorResponse;
import fr.eni.bookhub.exception.OperationException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {

        // Extraction générique des champs et des messages
        List<FieldErrorResponse> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> new FieldErrorResponse(
                        ((FieldError) error).getField(),
                        error.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        return new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                errors
        );
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
