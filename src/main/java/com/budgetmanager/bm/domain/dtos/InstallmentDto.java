package com.budgetmanager.bm.domain.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Builder;

@Builder
public record InstallmentDto(
    UUID id,
    TransactionDto transactionDto,
    Integer installmentNumber,
    Integer totalInstallments,
    BigDecimal amount,
    LocalDate dueDate
) {}
