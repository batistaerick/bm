package com.budgetmanager.bm.services;

import com.budgetmanager.bm.domain.entities.Category;
import com.budgetmanager.bm.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository repository;

    public Category save(Category category) {
        return repository.save(category);
    }
}
