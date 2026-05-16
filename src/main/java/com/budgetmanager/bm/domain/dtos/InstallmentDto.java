package com.budgetmanager.bm.domain.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Builder;

@Builder
public record InstallmentDto(
    UUID id,
    TransactionDto transaction,
    Integer installmentNumber,
    Integer totalInstallments,
    BigDecimal amount,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    LocalDate dueDate
) {}
