package com.budgetmanager.bm.converters;

import com.budgetmanager.bm.domain.dtos.TransactionDto;
import com.budgetmanager.bm.domain.entities.Transaction;

public class TransactionConverter {

    private TransactionConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static TransactionDto entityToDto(Transaction entity) {
        return TransactionDto.builder()
            .id(entity.getId())
            .notes(entity.getNotes())
            .repeats(entity.getRepeats())
            .endDate(entity.getEndDate())
            .startDate(entity.getStartDate())
            .totalValue(entity.getTotalValue())
            .installmentNumbers(entity.getInstallmentNumbers())
            .categoryDto(CategoryConverter.entityToDto(entity.getCategory()))
            .installments(
                entity
                    .getInstallments()
                    .stream()
                    .map(InstallmentConverter::entityToDto)
                    .toList()
            )
            .build();
    }

    public static Transaction dtoToEntity(TransactionDto dto) {
        return Transaction.builder()
            .id(dto.id())
            .notes(dto.notes())
            .repeats(dto.repeats())
            .endDate(dto.endDate())
            .startDate(dto.startDate())
            .totalValue(dto.totalValue())
            .installmentNumbers(dto.installmentNumbers())
            .category(CategoryConverter.dtoToEntity(dto.categoryDto()))
            .installments(
                dto
                    .installments()
                    .stream()
                    .map(InstallmentConverter::dtoToEntity)
                    .toList()
            )
            .build();
    }
}
