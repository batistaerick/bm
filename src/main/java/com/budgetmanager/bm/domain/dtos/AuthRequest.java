package com.budgetmanager.bm.domain.dtos;

import lombok.Builder;

@Builder
public record AuthRequest(
    String email,
    String password
) {
}
