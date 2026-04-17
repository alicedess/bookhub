package fr.eni.bookhub.dto.error;

import java.time.LocalDateTime;
import java.util.List;

public record ApiErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        List<FieldErrorResponse> errors
) {}