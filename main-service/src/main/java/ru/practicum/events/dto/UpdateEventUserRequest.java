package ru.practicum.events.dto;

import lombok.*;
import ru.practicum.events.model.StateActionUser;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEventUserRequest {
    private String title;
    private String annotation;
    private String description;
    private LocalDateTime eventDate;
    private Long category;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateActionUser stateAction;
}
