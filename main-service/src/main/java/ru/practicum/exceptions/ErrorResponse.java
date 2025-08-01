package ru.practicum.exceptions;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
    private String message;
    private String reason;
    private String status;
    private LocalDateTime timestamp;
}