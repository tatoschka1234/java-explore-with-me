package ru.practicum.events.dto;

import lombok.*;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.events.model.EventState;
import ru.practicum.users.dto.UserShortDto;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventFullDto {
    private Long id;
    private String title;
    private String annotation;
    private String description;
    private LocalDateTime createdOn;
    private LocalDateTime eventDate;
    private LocalDateTime publishedOn;
    private boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    private int confirmedRequests;
    private long views;
    private CategoryDto category;
    private UserShortDto initiator;
    private EventState state;
    private LocationDto location;
}

