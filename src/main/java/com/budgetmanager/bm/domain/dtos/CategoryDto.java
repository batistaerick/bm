package com.budgetmanager.bm.domain.dtos;

import com.budgetmanager.bm.enums.TransactionType;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CategoryDto(
    UUID id,
    String name,
    TransactionType transactionType
) {
}
