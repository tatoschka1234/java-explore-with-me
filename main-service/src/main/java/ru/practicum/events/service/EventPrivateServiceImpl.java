package ru.practicum.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.categories.service.CategoryService;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UpdateEventUserRequest;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventState;
import ru.practicum.events.model.StateActionUser;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.users.model.User;
import ru.practicum.users.model.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventPrivateServiceImpl implements EventPrivateService {

    private final EventRepository eventRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    @Override
    public List<EventShortDto> getUserEvents(Long userId) {
        List<Event> events = eventRepository.findByInitiatorId(userId);
        return events.stream()
                .map(event -> EventMapper.toShortDto(event, 0L))
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto dto) {
        User initiator = userService.getUser(userId);
        Event event = EventMapper.toEntity(dto);
        event.setInitiator(initiator);
        event.setCategory(categoryService.getCategoryEntity(dto.getCategory()));
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        event.setConfirmedRequests(0);
        event.setViews(0L);
        Event saved = eventRepository.save(event);
        return EventMapper.toFullDto(saved, 0L);
    }

    @Override
    public EventFullDto getUserEventById(Long userId, Long eventId) {
        Event event = getUserEventOrThrow(userId, eventId);
        return EventMapper.toFullDto(event, 0L);
    }

    @Override
    public EventFullDto updateUserEvent(Long userId, Long eventId, UpdateEventUserRequest dto) {
        Event event = getUserEventOrThrow(userId, eventId);

        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Нельзя редактировать опубликованное событие");
        }

        if (dto.getAnnotation() != null) event.setAnnotation(dto.getAnnotation());
        if (dto.getDescription() != null) event.setDescription(dto.getDescription());
        if (dto.getTitle() != null) event.setTitle(dto.getTitle());
        if (dto.getEventDate() != null) event.setEventDate(dto.getEventDate());
        if (dto.getCategory() != null)
            event.setCategory(categoryService.getCategoryEntity(dto.getCategory()));
        if (dto.getPaid() != null) event.setPaid(dto.getPaid());
        if (dto.getParticipantLimit() != null) event.setParticipantLimit(dto.getParticipantLimit());
        if (dto.getRequestModeration() != null) event.setRequestModeration(dto.getRequestModeration());

        if (dto.getStateAction() != null) {
            if (dto.getStateAction() == StateActionUser.SEND_TO_REVIEW) {
                event.setState(EventState.PENDING);
            } else if (dto.getStateAction() == StateActionUser.CANCEL_REVIEW) {
                event.setState(EventState.CANCELED);
            }
        }

        return EventMapper.toFullDto(eventRepository.save(event), 0L);
    }

    private Event getUserEventOrThrow(Long userId, Long eventId) {
        return eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено для пользователя"));
    }
}
