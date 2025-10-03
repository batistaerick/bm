package com.budgetmanager.bm.domain.dtos;

import java.sql.Blob;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserImageDto(
    UUID id,
    String name,
    String type,
    Blob profileImage
) {}
