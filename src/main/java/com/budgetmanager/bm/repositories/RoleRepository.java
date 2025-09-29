package com.budgetmanager.bm.repositories;

import com.budgetmanager.bm.domain.entities.Role;
import com.budgetmanager.bm.enums.UserRole;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByRoleName(UserRole userRole);
}
