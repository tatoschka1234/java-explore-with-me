package ru.practicum.categories.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.categories.dto.*;
import ru.practicum.categories.mapper.CategoryMapper;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;

import java.util.Optional;
@Service
@Transactional
@RequiredArgsConstructor
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private final CategoryRepository categoryRepository;

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
    public CategoryDto update(CategoryDto dto) {
        Category category = categoryRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException("Категория с id " + dto.getId() + " не найдена."));

        if (!category.getName().equals(dto.getName())) {
            Optional<Category> existing = categoryRepository.findByName(dto.getName());
            if (existing.isPresent()) {
                throw new ConflictException("Категория с именем '" + dto.getName() + "' уже существует.");
            }
        }

        category.setName(dto.getName());
        Category updated = categoryRepository.save(category);
        return CategoryMapper.toDto(updated);
    }

    @Override
    public void delete(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория с id " + catId + " не найдена."));

        // можно добавить проверку на связанные события

        categoryRepository.delete(category);
    }
}
