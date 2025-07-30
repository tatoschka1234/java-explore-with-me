package ru.practicum.stat.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EndpointHitDto {
    @NotBlank(message = "App name is required")
    private String app;

    @NotBlank(message = "URI is required")
    private String uri;

    @NotBlank(message = "IP address is required")
    private String ip;

    @NotNull(message = "Timestamp is required")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}