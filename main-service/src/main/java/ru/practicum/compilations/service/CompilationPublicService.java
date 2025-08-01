package ru.practicum.compilations.service;

import ru.practicum.compilations.dto.CompilationDto;

import java.util.List;

public interface CompilationPublicService {
    List<CompilationDto> getAll(Boolean pinned, int from, int size);
    CompilationDto getById(Long compId);
}
