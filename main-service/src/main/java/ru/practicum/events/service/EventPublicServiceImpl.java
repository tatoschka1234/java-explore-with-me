package ru.practicum.events.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.events.dto.EventFilterRequest;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventState;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.stat.client.StatsClient;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventPublicServiceImpl implements EventPublicService {
    private static final Pattern EVENT_URI = Pattern.compile("^/events/(\\d+)$");

    private final EventRepository eventRepository;
    private final StatsClient statsClient;

    @Value("${app.name:main-service}")
    private String appName;

    @Override
    public List<EventShortDto> getEvents(EventFilterRequest filter, HttpServletRequest request) {
        saveHit(request);

        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());
        Page<Event> events = eventRepository.searchPublishedEvents(
                filter.getText(), filter.getPaid(), filter.getRangeStart(), filter.getRangeEnd(), pageable
        );

        List<String> uris = events.getContent().stream()
                .map(e -> "/events/" + e.getId())
                .toList();

        Map<Long, Long> viewsMap = getViews(uris);

        return events.getContent().stream()
                .map(event -> {
                    long views = viewsMap.getOrDefault(event.getId(), 0L);
                    return EventMapper.toShortDto(event, views);
                })
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventById(Long eventId, HttpServletRequest request) {
        saveHit(request);

        Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Event not found or not published"));

        long views = getViews(List.of("/events/" + event.getId()))
                .getOrDefault(event.getId(), 0L);

        return EventMapper.toFullDto(event, views);
    }

    private void saveHit(HttpServletRequest request) {
        statsClient.saveHit(EndpointHitDto.builder()
                .app(appName)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build());
    }


    private Map<Long, Long> getViews(List<String> uris) {
        if (uris == null || uris.isEmpty()) return Map.of();


        LocalDateTime start = LocalDateTime.now().minusYears(40);
        LocalDateTime end   = LocalDateTime.now();

        List<ViewStatsDto> stats = statsClient.getStats(start, end, uris, false); // unique=false

        return stats.stream()
                .map(vs -> {
                    String uri = vs.getUri();
                    if (uri == null) return null;
                    Matcher m = EVENT_URI.matcher(uri);
                    return m.matches() ? Map.entry(Long.valueOf(m.group(1)), vs.getHits()) : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum));
    }


}
