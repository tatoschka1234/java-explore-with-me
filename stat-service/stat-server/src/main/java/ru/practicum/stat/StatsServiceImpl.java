package ru.practicum.stat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.dto.ViewStatsDto;
import ru.practicum.stat.entity.EndpointHit;
import ru.practicum.stat.mapper.StatsMapper;
import ru.practicum.stat.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository repository;

    @Override
    public void saveHit(EndpointHitDto dto) {
        EndpointHit hit = StatsMapper.toEntity(dto);
        repository.save(hit);
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date must be before end date.");
        }
        if (uris == null || uris.isEmpty()) {
            uris = repository.findAll().stream().map(EndpointHit::getUri).distinct().toList();
        }
        return unique
                ? repository.getUniqueStats(start, end, uris)
                : repository.getAllStats(start, end, uris);
    }
}
