package com.budgetmanager.bm.converters;

import com.budgetmanager.bm.domain.dtos.SavingDto;
import com.budgetmanager.bm.domain.entities.Saving;

public class SavingConverter {

    public static Saving dtoToEntity(SavingDto dto) {
        return Saving.builder()
            .id(dto.id())
            .name(dto.name())
            .amount(dto.amount())
            .location(dto.location())
            .build();
    }

    public static SavingDto entityToDto(Saving saving) {
        return SavingDto.builder()
            .id(saving.getId())
            .name(saving.getName())
            .amount(saving.getAmount())
            .location(saving.getLocation())
            .build();
    }
}
