package ru.practicum.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.mapper.CategoryMapper;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryPublicServiceImpl implements CategoryPublicService {

    private final CategoryRepository repository;

    @Override
    public List<CategoryDto> getAll(int from, int size) {
        return repository.findAll(PageRequest.of(from / size, size))
                .stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(Long catId) {
        return CategoryMapper.toDto(repository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category not found")));
    }
}