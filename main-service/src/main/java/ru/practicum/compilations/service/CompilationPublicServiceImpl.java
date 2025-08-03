package ru.practicum.compilations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.mapper.CompilationMapper;
import ru.practicum.compilations.repository.CompilationRepository;
import ru.practicum.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationPublicServiceImpl implements CompilationPublicService {

    private final CompilationRepository repository;

    @Override
    public List<CompilationDto> getAll(Boolean pinned, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);

        if (pinned != null) {
            return repository.findAllByPinned(pinned).stream()
                    .skip(from).limit(size)
                    .map(CompilationMapper::toDto)
                    .collect(Collectors.toList());
        }

        return repository.findAll(pageRequest).stream()
                .map(CompilationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getById(Long compId) {
        return repository.findById(compId)
                .map(CompilationMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Compilation not found"));
    }
}
