package com.budgetmanager.bm.services;

import com.budgetmanager.bm.domain.entities.RefreshToken;
import com.budgetmanager.bm.domain.entities.User;
import com.budgetmanager.bm.repositories.RefreshTokenRepository;
import com.budgetmanager.bm.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${jwt.refresh-expiration}")
    private String refreshTokenDurationDays;

    public RefreshToken createRefreshToken(User user) {
        RefreshToken token = RefreshToken.builder()
            .token(UUID.randomUUID().toString())
            .user(user)
            .expiryDate(Instant.now().plusSeconds(Integer.parseInt(refreshTokenDurationDays) * 24L * 3600L))
            .build();
        return refreshTokenRepository.save(token);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired. Please login again.");
        }
        return token;
    }

    public int deleteByUser(User user) {
        return refreshTokenRepository.deleteByUser(user);
    }
}
