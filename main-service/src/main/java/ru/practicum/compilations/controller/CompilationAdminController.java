package ru.practicum.compilations.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.dto.UpdateCompilationRequest;
import ru.practicum.compilations.service.CompilationAdminService;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class CompilationAdminController {

    private final CompilationAdminService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@RequestBody @Valid NewCompilationDto dto) {
        return service.create(dto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long compId) {
        service.delete(compId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addEvent(@PathVariable Long compId, @PathVariable Long eventId) {
        service.addEvent(compId, eventId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeEvent(@PathVariable Long compId, @PathVariable Long eventId) {
        service.removeEvent(compId, eventId);
    }

    @PatchMapping("/{compId}/pin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void pin(@PathVariable Long compId) {
        service.pin(compId);
    }

    @DeleteMapping("/{compId}/pin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unpin(@PathVariable Long compId) {
        service.unpin(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto update(@PathVariable Long compId,
                                 @RequestBody @Valid UpdateCompilationRequest dto) {
        return service.update(compId, dto);
    }
}

