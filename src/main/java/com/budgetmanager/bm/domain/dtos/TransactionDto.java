package com.budgetmanager.bm.domain.dtos;

import com.budgetmanager.bm.enums.RepeatInterval;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record TransactionDto(
    UUID id,
    @Valid @NotNull CategoryDto category,
    String notes,
    @NotNull @Positive BigDecimal totalValue,
    @Max(value = 500, message = "Value cannot exceed 500")
    @Min(value = 2, message = "Minimum value is 2")
    Integer installmentNumbers,
    List<InstallmentDto> installments,
    RepeatInterval repeats,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    @NotNull
    LocalDate date
) {}
