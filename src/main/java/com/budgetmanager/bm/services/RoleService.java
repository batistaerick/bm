package com.budgetmanager.bm.services;

import com.budgetmanager.bm.converters.RoleConverter;
import com.budgetmanager.bm.domain.dtos.RoleDto;
import com.budgetmanager.bm.domain.entities.Role;
import com.budgetmanager.bm.enums.UserRole;
import com.budgetmanager.bm.exceptions.GlobalException;
import com.budgetmanager.bm.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository repository;

    public Role save(RoleDto roleDto) {
        Role role = RoleConverter.dtoToEntity(roleDto);
        return repository.save(role);
    }

    public Role findByRoleName(UserRole userRole) {
        return repository
            .findByRoleName(userRole)
            .orElseThrow(
                () -> new GlobalException(
                    HttpStatus.NOT_FOUND,
                    "Role not found for {}",
                    userRole
                )
            );
    }
}
