package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewEventDto {

    @NotBlank
    @Size(min = 3, max = 120, message = "Title must be between 3 and 120 characters")
    private String title;

    @NotBlank
    @Size(min = 20, max = 2000, message = "annotation must be between 20 and 2000 characters")
    private String annotation;

    @NotBlank
    @Size(min = 20, max = 7000, message = "Description must be between 20 and 7000 characters")
    private String description;

    @NotNull
    @Future(message = "Event date must be in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull
    private Long category;

    private Boolean paid = false;

    @PositiveOrZero(message = "Participant limit must be zero or positive")
    @Builder.Default
    private Integer participantLimit = 0;

    private Boolean requestModeration = true;

    @NotNull @Valid
    private LocationDto location;
}
