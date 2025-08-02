package ru.practicum.events.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.events.dto.EventFilterRequest;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;

import java.util.List;

public interface EventPublicService {
    List<EventShortDto> getEvents(EventFilterRequest filter, HttpServletRequest request);

    EventFullDto getEventById(Long eventId, HttpServletRequest request);
}
