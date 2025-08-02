package ru.practicum.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNotFound(final NotFoundException e) {
        log.warn("404 Not Found: {}", e.getMessage());
        return buildError(e.getMessage(), "Object not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleBadRequest(final BadRequestException e) {
        log.warn("400 Bad Request: {}", e.getMessage());
        return buildError(e.getMessage(), "Invalid request", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleConflict(final IllegalStateException e) {
        log.warn("409 Conflict: {}", e.getMessage());
        return buildError(e.getMessage(), "Integrity constraint violation", HttpStatus.CONFLICT);
    }

    private ResponseEntity<ErrorResponse> buildError(String message, String reason, HttpStatus status) {
        ErrorResponse body = ErrorResponse.builder()
                .message(message)
                .reason(reason)
                .status(status.name())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleMissingRequestParam(MissingServletRequestParameterException ex) {
        return ErrorResponse.builder()
                .message("Missing required parameter: " + ex.getParameterName())
                .reason("MissingServletRequestParameterException")
                .status(HttpStatus.BAD_REQUEST.name())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("Field '%s' %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining("; "));

        return ErrorResponse.builder()
                .message(message)
                .reason("Validation failed")
                .status(HttpStatus.BAD_REQUEST.name())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidBody(HttpMessageNotReadableException ex) {
        return ErrorResponse.builder()
                .message("Request body is missing or malformed")
                .reason("InvalidRequestBody")
                .status(HttpStatus.BAD_REQUEST.name())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenException e) {
        log.warn("403 Forbidden: {}", e.getMessage());
        return buildError(e.getMessage(), "Forbidden", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(final ConflictException e) {
        log.warn("409 Conflict: {}", e.getMessage());
        return buildError(e.getMessage(), "Integrity constraint violation", HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowableException(final Exception e) {
        return ErrorResponse.builder()
                .message(e.getMessage())
                .reason("Internal server error occurred")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .timestamp(LocalDateTime.now())
                .build();
    }


}