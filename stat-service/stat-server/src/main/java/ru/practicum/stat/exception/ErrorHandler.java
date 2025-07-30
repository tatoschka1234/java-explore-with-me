package ru.practicum.stat.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidation(ConstraintViolationException ex) {
        return Map.of(
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Validation error",
                "message", ex.getMessage(),
                "timestamp", LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleIllegalArgument(IllegalArgumentException ex) {
        return Map.of(
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Invalid input",
                "message", ex.getMessage(),
                "timestamp", LocalDateTime.now()
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleMissingRequestParam(MissingServletRequestParameterException ex) {
        return Map.of(
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Missing request parameter",
                "message", ex.getParameterName() + " parameter is missing",
                "timestamp", LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleGeneral(Exception ex) {
        return Map.of(
                "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "error", "Internal server error",
                "message", ex.getMessage(),
                "timestamp", LocalDateTime.now()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return Map.of(
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Validation failed",
                "message", ex.getBindingResult().getFieldErrors().stream()
                        .map(error -> error.getField() + ": " + error.getDefaultMessage())
                        .toList(),
                "timestamp", LocalDateTime.now()
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return Map.of(
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Invalid parameter format",
                "message", String.format("Parameter '%s' has invalid format: '%s'", ex.getName(), ex.getValue()),
                "timestamp", LocalDateTime.now()
        );
    }


}
