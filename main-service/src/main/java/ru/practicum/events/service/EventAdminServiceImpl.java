package ru.practicum.events.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventStateAction;
import ru.practicum.events.dto.UpdateEventAdminRequest;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventState;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EventAdminServiceImpl implements EventAdminService {

    private final EventRepository eventRepo;
    private final CategoryRepository categoryRepo;

    @Override
    public List<EventFullDto> getEvents(List<Long> users,
                                               List<EventState> states,
                                               List<Long> categories,
                                               LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd,
                                               Integer from,
                                               Integer size) {

        if (rangeStart != null && rangeEnd != null) {
            if (!rangeStart.isBefore(rangeEnd)) {
                throw new BadRequestException(
                        String.format("rangeStart must be before rangeEnd (got %s .. %s)", rangeStart, rangeEnd)
                );
            }
        }

        if (users != null && users.isEmpty()) users = null;
        if (states != null && states.isEmpty()) states = null;
        if (categories != null && categories.isEmpty()) categories = null;

        Pageable pageable = PageRequest.of(from / size, size);

        Page<Event> page = eventRepo.getEventsByAdmin(
                users, states, categories, rangeStart, rangeEnd, pageable
        );

        return page.stream()
                .map(e -> EventMapper.toFullDto(e, e.getViews()))
                .toList();
    }



    @Transactional
    @Override
    public EventFullDto updateEvent(long id, UpdateEventAdminRequest r) {
        Event e = eventRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        if (r.getStateAction() != null) {
            if (e.getState() != EventState.PENDING) {
                throw new ConflictException("Публиковать можно только из состояния PENDING");
            }
            if (r.getStateAction() == EventStateAction.PUBLISH_EVENT) {
                e.setState(EventState.PUBLISHED);
                e.setPublishedOn(LocalDateTime.now());
            } else if (r.getStateAction() == EventStateAction.REJECT_EVENT) {
                e.setState(EventState.CANCELED);
            }
        }

        if (r.getCategory() != null) {
            Category category = categoryRepo.findById(r.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category " + r.getCategory() + " not found"));
            e.setCategory(category);
        }
        if (r.getTitle() != null) e.setTitle(r.getTitle());
        if (r.getAnnotation() != null) e.setAnnotation(r.getAnnotation());
        if (r.getDescription() != null) e.setDescription(r.getDescription());
        if (r.getParticipantLimit() != null) e.setParticipantLimit(r.getParticipantLimit());
        if (r.getRequestModeration() != null) e.setRequestModeration(r.getRequestModeration());
        if (r.getPaid() != null) e.setPaid(r.getPaid());

        Event saved = eventRepo.save(e);
        return EventMapper.toFullDto(saved, saved.getViews());
    }

}
