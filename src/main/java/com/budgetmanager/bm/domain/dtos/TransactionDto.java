package com.budgetmanager.bm.domain.dtos;

import com.budgetmanager.bm.enums.RepeatInterval;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record TransactionDto(
    UUID id,
    CategoryDto category,
    String notes,
    BigDecimal totalValue,
    @Max(value = 500, message = "Value cannot exceed 500")
    @Min(value = 2, message = "Minimum value of 0")
    Integer installmentNumbers,
    List<InstallmentDto> installments,
    RepeatInterval repeats,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate date
) {}
