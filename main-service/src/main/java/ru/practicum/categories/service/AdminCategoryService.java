package ru.practicum.categories.service;

import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;

public interface AdminCategoryService {
    CategoryDto create(NewCategoryDto dto);
    CategoryDto update(CategoryDto dto);
    void delete(Long catId);
}
