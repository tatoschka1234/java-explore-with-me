package ru.practicum.events.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.*;
import ru.practicum.events.service.EventPrivateService;


import java.util.List;

//@RestController
//@RequestMapping("/users")
//@RequiredArgsConstructor
//public class PrivateEventController {
//
//    private final EventPrivateService eventPrivateService;
//
//    @GetMapping("/{userId}/events")
//    public List<EventShortDto> getUserEvents(@PathVariable Long userId) {
//        return eventPrivateService.getUserEvents(userId);
//    }
//
//    @PostMapping("/{userId}/events")
//    public EventFullDto createEvent(@PathVariable Long userId,
//                                    @RequestBody @Valid NewEventDto dto) {
//        return eventPrivateService.createEvent(userId, dto);
//    }
//
//    @GetMapping("/{eventId}")
//    public EventFullDto getUserEventById(@PathVariable Long userId,
//                                         @PathVariable Long eventId) {
//        return eventPrivateService.getUserEventById(userId, eventId);
//    }
//
//    @PatchMapping("/{eventId}")
//    public EventFullDto updateEvent(@PathVariable Long userId,
//                                    @PathVariable Long eventId,
//                                    @RequestBody @Valid UpdateEventUserRequest dto) {
//        return eventPrivateService.updateUserEvent(userId, eventId, dto);
//    }
//}

@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
public class PrivateEventController {

    private final EventPrivateService eventPrivateService;

    @GetMapping("/events")
    public List<EventShortDto> getUserEvents(@PathVariable Long userId) {
        return eventPrivateService.getUserEvents(userId);
    }

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable Long userId,
                                    @RequestBody @Valid NewEventDto dto) {
        return eventPrivateService.createEvent(userId, dto);
    }

    @GetMapping("/events/{eventId}")
    public EventFullDto getUserEventById(@PathVariable Long userId,
                                         @PathVariable Long eventId) {
        return eventPrivateService.getUserEventById(userId, eventId);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @RequestBody @Valid UpdateEventUserRequest dto) {
        return eventPrivateService.updateUserEvent(userId, eventId, dto);
    }
}
