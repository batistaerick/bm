package com.budgetmanager.bm.domain.dtos;

import java.util.List;
import java.util.UUID;

public record AuthenticatedUser(UUID id, String username, List<String> roles) {}
