package com.budgetmanager.bm.domain.dtos;

import com.budgetmanager.bm.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;

@Builder
public record CategoryDto(
    UUID id,
    @NotBlank String name,
    @NotNull TransactionType transactionType
) {}
