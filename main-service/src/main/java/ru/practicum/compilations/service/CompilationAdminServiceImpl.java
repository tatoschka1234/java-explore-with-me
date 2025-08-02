package ru.practicum.compilations.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.dto.UpdateCompilationRequest;
import ru.practicum.compilations.mapper.CompilationMapper;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.repository.CompilationRepository;
import ru.practicum.compilations.service.CompilationAdminService;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.NotFoundException;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationAdminServiceImpl implements CompilationAdminService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto create(NewCompilationDto dto) {
        Compilation compilation = Compilation.builder()
                .title(dto.getTitle())
                .pinned(dto.isPinned())
                .events(dto.getEvents() == null ? new HashSet<>() :
                        dto.getEvents().stream()
                                .map(this::getEvent)
                                .collect(Collectors.toSet()))
                .build();

        return CompilationMapper.toDto(compilationRepository.save(compilation));
    }

    @Override
    public void delete(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    public void addEvent(Long compId, Long eventId) {
        Compilation compilation = getCompilation(compId);
        compilation.getEvents().add(getEvent(eventId));
        compilationRepository.save(compilation);
    }

    @Override
    public void removeEvent(Long compId, Long eventId) {
        Compilation compilation = getCompilation(compId);
        compilation.getEvents().removeIf(event -> event.getId().equals(eventId));
        compilationRepository.save(compilation);
    }

    @Override
    public void pin(Long compId) {
        Compilation compilation = getCompilation(compId);
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }

    @Override
    public void unpin(Long compId) {
        Compilation compilation = getCompilation(compId);
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    private Compilation getCompilation(Long id) {
        return compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Compilation not found"));
    }

    private Event getEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));
    }

    @Transactional
    public CompilationDto update(Long compId, UpdateCompilationRequest dto) {
        Compilation c = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found"));

        if (dto.getTitle() != null)  c.setTitle(dto.getTitle());
        if (dto.getPinned() != null) c.setPinned(dto.getPinned());

        if (dto.getEvents() != null) {
            Set<Event> events = dto.getEvents().isEmpty()
                    ? new HashSet<>()
                    : new HashSet<>(eventRepository.findAllById(dto.getEvents()));
            c.setEvents(events);
        }

        return CompilationMapper.toDto(compilationRepository.save(c));
    }
}
