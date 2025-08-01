package ru.practicum.events.dto;

import lombok.*;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.users.dto.UserShortDto;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventShortDto {
    private Long id;
    private String title;
    private String annotation;
    private LocalDateTime eventDate;
    private boolean paid;
    private long views;
    private int confirmedRequests;
    private CategoryDto category;
    private UserShortDto initiator;
}