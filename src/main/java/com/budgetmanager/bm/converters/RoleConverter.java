package com.budgetmanager.bm.converters;

import com.budgetmanager.bm.domain.dtos.RoleDto;
import com.budgetmanager.bm.domain.entities.Role;

public class RoleConverter {
    private RoleConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static RoleDto entityToDto(Role entity) {

        return RoleDto
            .builder()
            .id(entity.getId())
            .roleName(entity.getRoleName())
            .build();
    }

    public static Role dtoToEntity(RoleDto dto) {
        return Role
            .builder()
            .id(dto.id())
            .roleName(dto.roleName())
            .build();
    }
}