package com.budgetmanager.bm.domain.dtos;

import com.budgetmanager.bm.enums.TransactionType;
import java.util.UUID;
import lombok.Builder;

@Builder
public record CategoryDto(
    UUID id,
    String name,
    TransactionType transactionType
) {}
