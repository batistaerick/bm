package com.budgetmanager.bm.domain.dtos;

import com.budgetmanager.bm.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;

@Builder
public record RoleDto(UUID id, @NotNull UserRole roleName) {}
