package ru.practicum.stat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.dto.ViewStatsDto;
import ru.practicum.stat.dto.ViewStatsRequestDto;
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
    public List<ViewStatsDto> getStats(ViewStatsRequestDto request) {
        LocalDateTime start = request.getStart();
        LocalDateTime end = request.getEnd();
        List<String> uris = request.getUris();
        boolean unique = request.isUnique();

        if (uris == null || uris.isEmpty()) {
            return unique
                    ? repository.findStatsWithUniqueIp(start, end)
                    : repository.findStats(start, end);
        }

        return unique
                ? repository.findStatsWithUniqueIp(start, end, uris)
                : repository.findStats(start, end, uris);
    }

}
