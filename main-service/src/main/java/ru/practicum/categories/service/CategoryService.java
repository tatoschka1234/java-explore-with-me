package ru.practicum.categories.service;

import ru.practicum.categories.model.Category;

public interface CategoryService {
    Category getById(Long id);
    Category getCategoryEntity(Long catId);
}