package com.budgetmanager.bm.services;

import com.budgetmanager.bm.converters.CategoryConverter;
import com.budgetmanager.bm.domain.dtos.CategoryDto;
import com.budgetmanager.bm.domain.entities.Category;
import com.budgetmanager.bm.repositories.CategoryRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    public Category save(CategoryDto dto) {
        return repository.save(CategoryConverter.dtoToEntity(dto));
    }

    public Optional<Category> findById(UUID id) {
        return repository.findById(id);
    }
}
