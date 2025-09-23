package com.budgetmanager.bm.repositories;

import com.budgetmanager.bm.domain.entities.RefreshToken;
import com.budgetmanager.bm.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);

    int deleteByUser(User user);
}
