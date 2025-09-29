package com.budgetmanager.bm.services;

import com.budgetmanager.bm.converters.CategoryConverter;
import com.budgetmanager.bm.domain.dtos.CategoryDto;
import com.budgetmanager.bm.domain.entities.Category;
import com.budgetmanager.bm.enums.TransactionType;
import com.budgetmanager.bm.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    public Category save(CategoryDto dto) {
        return repository.save(CategoryConverter.dtoToEntity(dto));
    }

    public List<Category> findByTransactionType(TransactionType transactionType) {
        return repository.findByTransactionType(transactionType);
    }

    public Optional<Category> findById(UUID id) {
        return repository.findById(id);
    }
}
