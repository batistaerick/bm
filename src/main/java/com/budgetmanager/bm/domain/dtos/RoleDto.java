package com.budgetmanager.bm.domain.dtos;

import com.budgetmanager.bm.enums.UserRole;
import java.util.UUID;
import lombok.Builder;

@Builder
public record RoleDto(UUID id, UserRole roleName) {}
