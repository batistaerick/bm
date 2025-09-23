package com.budgetmanager.bm.domain.dtos;

import com.budgetmanager.bm.enums.UserRole;
import lombok.Builder;

import java.util.UUID;

@Builder
public record RoleDto(
    UUID id,
    UserRole roleName
) {
}
