package com.budgetmanager.bm.domain.dtos;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
public record InstallmentDto(
    UUID id,
    TransactionDto transactionDto,
    Integer installmentNumber,
    Integer totalInstallments,
    BigDecimal amount,
    Instant dueDate
) {
}
