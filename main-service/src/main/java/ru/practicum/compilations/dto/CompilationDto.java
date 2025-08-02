package ru.practicum.compilations.dto;

import lombok.*;
import ru.practicum.events.dto.EventShortDto;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompilationDto {
    private Long id;
    private String title;
    private boolean pinned;
    private Set<EventShortDto> events;
}
