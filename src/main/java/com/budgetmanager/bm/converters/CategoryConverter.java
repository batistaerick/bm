package com.budgetmanager.bm.converters;

import com.budgetmanager.bm.domain.dtos.CategoryDto;
import com.budgetmanager.bm.domain.entities.Category;

public class CategoryConverter {

    private CategoryConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static CategoryDto entityToDto(Category entity) {
        return CategoryDto.builder()
            .id(entity.getId())
            .name(entity.getName())
            .transactionType(entity.getTransactionType())
            .build();
    }

    public static Category dtoToEntity(CategoryDto dto) {
        return Category.builder()
            .id(dto.id())
            .name(dto.name())
            .transactionType(dto.transactionType())
            .build();
    }
}
