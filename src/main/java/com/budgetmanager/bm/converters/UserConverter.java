package com.budgetmanager.bm.converters;

import com.budgetmanager.bm.domain.dtos.RoleDto;
import com.budgetmanager.bm.domain.dtos.UserDto;
import com.budgetmanager.bm.domain.entities.Role;
import com.budgetmanager.bm.domain.entities.User;
import java.util.Set;
import java.util.stream.Collectors;

public class UserConverter {

    private UserConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static UserDto entityToDto(User entity) {
        return UserDto.builder()
            .id(entity.getId())
            .email(entity.getEmail())
            .name(entity.getName())
            .rolesDto(checkRoles(entity))
            .build();
    }

    public static User dtoToEntity(UserDto dto) {
        return User.builder()
            .id(dto.id())
            .email(dto.email())
            .name(dto.name())
            .password(dto.password())
            .roles(checkRoles(dto))
            .build();
    }

    public static Set<Role> checkRoles(UserDto dto) {
        if (dto.rolesDto() != null) {
            return dto
                .rolesDto()
                .stream()
                .map(RoleConverter::dtoToEntity)
                .collect(Collectors.toSet());
        }
        return Set.of(new Role());
    }

    public static Set<RoleDto> checkRoles(User user) {
        if (user.getRoles() != null) {
            return user
                .getRoles()
                .stream()
                .map(RoleConverter::entityToDto)
                .collect(Collectors.toSet());
        }
        return Set.of(RoleDto.builder().build());
    }
}
