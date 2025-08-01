package ru.practicum.compilations.service;

import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;

public interface CompilationAdminService {
    CompilationDto create(NewCompilationDto dto);
    void delete(Long compId);
    void addEvent(Long compId, Long eventId);
    void removeEvent(Long compId, Long eventId);
    void pin(Long compId);
    void unpin(Long compId);
}