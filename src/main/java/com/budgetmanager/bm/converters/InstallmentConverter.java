package com.budgetmanager.bm.converters;

import com.budgetmanager.bm.domain.dtos.InstallmentDto;
import com.budgetmanager.bm.domain.entities.Installment;

public class InstallmentConverter {

    private InstallmentConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static InstallmentDto entityToDto(Installment entity) {
        entity.getTransaction().setInstallments(null);

        return InstallmentDto.builder()
            .id(entity.getId())
            .installmentNumber(entity.getInstallmentNumber())
            .totalInstallments(entity.getTotalInstallments())
            .amount(entity.getAmount())
            .transaction(
                TransactionConverter.entityToDto(entity.getTransaction())
            )
            .build();
    }

    public static Installment dtoToEntity(InstallmentDto dto) {
        return Installment.builder()
            .id(dto.id())
            .installmentNumber(dto.installmentNumber())
            .totalInstallments(dto.totalInstallments())
            .amount(dto.amount())
            .transaction(TransactionConverter.dtoToEntity(dto.transaction()))
            .build();
    }
}
