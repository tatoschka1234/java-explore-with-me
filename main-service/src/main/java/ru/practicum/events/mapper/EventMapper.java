package ru.practicum.events.mapper;

import ru.practicum.categories.mapper.CategoryMapper;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.LocationDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.Location;
import ru.practicum.users.model.UserMapper;

public class EventMapper {

    public static LocationDto toDto(Location loc) {
        if (loc == null) return null;
        return LocationDto.builder()
                .lat(loc.getLat())
                .lon(loc.getLon())
                .build();
    }

    public static Location toEntity(LocationDto dto) {
        if (dto == null) return null;
        return Location.builder()
                .lat(dto.getLat())
                .lon(dto.getLon())
                .build();
    }

    public static EventShortDto toShortDto(Event event, long views) {
        return EventShortDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .eventDate(event.getEventDate())
                .paid(event.isPaid())
                .views(views)
                .views(event.getViews())
                .confirmedRequests(event.getConfirmedRequests())
                .category(CategoryMapper.toDto(event.getCategory()))
                .initiator(UserMapper.toShortDto(event.getInitiator()))
                .build();
    }

    public static EventFullDto toFullDto(Event event, long views) {
        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .createdOn(event.getCreatedOn())
                .eventDate(event.getEventDate())
                .publishedOn(event.getPublishedOn())
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.isRequestModeration())
                .confirmedRequests(event.getConfirmedRequests())
                .views(event.getViews())
                .category(CategoryMapper.toDto(event.getCategory()))
                .initiator(UserMapper.toShortDto(event.getInitiator()))
                .state(event.getState())
                .views(views)
                .location(toDto(event.getLocation()))
                .build();
    }

    public static Event toEntity(NewEventDto dto) {
        return Event.builder()
                .title(dto.getTitle())
                .annotation(dto.getAnnotation())
                .description(dto.getDescription())
                .eventDate(dto.getEventDate())
                .paid(dto.getPaid() != null ? dto.getPaid() : false)
                .participantLimit(dto.getParticipantLimit())
                .requestModeration(dto.getRequestModeration() != null ? dto.getRequestModeration() : true)
                .location(toEntity(dto.getLocation()))
                .build();
    }

}
