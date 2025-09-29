package com.budgetmanager.bm.domain.dtos;

import java.util.Set;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserDto(
    UUID id,
    String name,
    String email,
    String password,
    Set<RoleDto> rolesDto
) {}
