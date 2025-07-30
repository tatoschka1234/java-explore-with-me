package ru.practicum.stat;

import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.dto.ViewStatsDto;
import ru.practicum.stat.dto.ViewStatsRequestDto;

import java.util.List;

public interface StatsService {
    void saveHit(EndpointHitDto dto);

    List<ViewStatsDto> getStats(ViewStatsRequestDto request);
}
