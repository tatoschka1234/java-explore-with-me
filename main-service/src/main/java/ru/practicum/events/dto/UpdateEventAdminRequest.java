package ru.practicum.events.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEventAdminRequest {
    private String title;
    private String annotation;
    private String description;
    private LocalDateTime eventDate;
    private Long category;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private EventStateAction stateAction;
    private LocationDto location;
}
