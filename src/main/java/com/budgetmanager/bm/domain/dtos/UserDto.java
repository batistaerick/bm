package com.budgetmanager.bm.domain.dtos;

import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record UserDto(
    UUID id,
    String name,
    String email,
    String password,
    Set<RoleDto> rolesDto
) {
}
