package ru.practicum.categories.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.categories.dto.*;
import ru.practicum.categories.mapper.CategoryMapper;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;

import java.util.Optional;
@Service
@Transactional
@RequiredArgsConstructor
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto create(NewCategoryDto dto) {
        Optional<Category> existing = categoryRepository.findByName(dto.getName());
        if (existing.isPresent()) {
            throw new ConflictException("Категория с именем '" + dto.getName() + "' уже существует.");
        }

        Category category = CategoryMapper.toEntity(dto);
        Category saved = categoryRepository.save(category);
        return CategoryMapper.toDto(saved);
    }

    @Override
    public CategoryDto update(Long catId, NewCategoryDto dto) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория с id " + catId + " не найдена."));

        String newName = dto.getName();

        if (!category.getName().equals(newName)) {
            categoryRepository.findByName(newName).ifPresent(c -> {
                throw new ConflictException("Категория с именем '" + newName + "' уже существует.");
            });
        }

        category.setName(newName);
        return CategoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void delete(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория с id " + catId + " не найдена."));

        if (eventRepository.existsByCategoryId(catId)) {
            throw new ConflictException("Нельзя удалить категорию: к ней привязаны события");
        }

        categoryRepository.delete(category);
    }
}
