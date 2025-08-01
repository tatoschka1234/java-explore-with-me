package ru.practicum.compilations.mapper;

import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.events.mapper.EventMapper;

import java.util.stream.Collectors;

public class CompilationMapper {

    public static CompilationDto toDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.isPinned())
                .events(compilation.getEvents().stream()
                        .map(event -> EventMapper.toShortDto(event, 0L))
                        .collect(Collectors.toSet()))
                .build();
    }
}
