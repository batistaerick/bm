package com.budgetmanager.bm.domain.dtos;

import com.budgetmanager.bm.enums.RepeatInterval;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
public record TransactionDto(
    UUID id,
    UserDto userDto,
    CategoryDto categoryDto,
    String notes,
    BigDecimal totalValue,
    Integer installments,
    RepeatInterval repeats,
    Instant startDate,
    Instant endDate
) {
}
