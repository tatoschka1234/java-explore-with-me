package ru.practicum.stat.mapper;

import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.dto.ViewStatsDto;
import ru.practicum.stat.entity.EndpointHit;


public class StatsMapper {

    public static EndpointHit toEntity(EndpointHitDto dto) {
        return EndpointHit.builder()
                .app(dto.getApp())
                .uri(dto.getUri())
                .ip(dto.getIp())
                .timestamp(dto.getTimestamp())
                .build();
    }

    public static ViewStatsDto toDto(String app, String uri, long hits) {
        return ViewStatsDto.builder()
                .app(app)
                .uri(uri)
                .hits(hits)
                .build();
    }
}
