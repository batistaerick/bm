package com.budgetmanager.bm.services;

import com.budgetmanager.bm.converters.CategoryConverter;
import com.budgetmanager.bm.domain.dtos.CategoryDto;
import com.budgetmanager.bm.domain.entities.Category;
import com.budgetmanager.bm.enums.TransactionType;
import com.budgetmanager.bm.exceptions.GlobalException;
import com.budgetmanager.bm.repositories.CategoryRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    public Category save(CategoryDto dto) {
        return repository.save(CategoryConverter.dtoToEntity(dto));
    }

    @Transactional
    public List<Category> saveAll(List<CategoryDto> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            throw new GlobalException(
                HttpStatus.BAD_REQUEST,
                "At least one category is required"
            );
        }
        return repository.saveAll(
            dtos.stream().map(CategoryConverter::dtoToEntity).toList()
        );
    }

    public List<Category> findByTransactionType(
        TransactionType transactionType
    ) {
        return repository.findByTransactionType(transactionType);
    }

    public Optional<Category> findById(UUID id) {
        return repository.findById(id);
    }
}
