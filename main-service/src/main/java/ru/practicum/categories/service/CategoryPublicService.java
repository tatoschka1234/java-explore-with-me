package ru.practicum.categories.service;

import ru.practicum.categories.dto.CategoryDto;

import java.util.List;

public interface CategoryPublicService {
    List<CategoryDto> getAll(int from, int size);
    CategoryDto getById(Long catId);
}