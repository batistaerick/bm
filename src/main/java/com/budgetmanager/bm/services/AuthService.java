package com.budgetmanager.bm.services;

import com.budgetmanager.bm.domain.entities.RefreshToken;
import com.budgetmanager.bm.domain.entities.User;
import com.budgetmanager.bm.exceptions.GlobalException;
import com.budgetmanager.bm.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Value("${jwt.refresh-expiration}")
    private int refreshExpiration;

    public String loginAndCreateAccessToken(String email, String password) {
        User user = userService
            .findByEmail(email)
            .orElseThrow(
                () -> new GlobalException(
                    HttpStatus.NOT_FOUND,
                    "User not found for {}",
                    email
                )
            );

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new GlobalException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        return jwtUtil.generateAccessToken(email);
    }

    public RefreshToken createRefreshToken(User user) {
        refreshTokenService.deleteByUser(user);

        RefreshToken refreshToken = RefreshToken
            .builder()
            .token(UUID.randomUUID() + "-" + UUID.randomUUID())
            .user(user)
            .expiryDate(Instant.now().plusMillis(refreshExpiration))
            .build();

        return refreshTokenService.save(refreshToken);
    }

    public boolean validateRefreshToken(String token) {
        return refreshTokenService
            .findByToken(token)
            .map(
                refreshToken -> refreshToken
                    .getExpiryDate()
                    .isAfter(Instant.now())
            )
            .orElse(false);
    }

    public RefreshToken rotateRefreshToken(String oldToken) {
        RefreshToken refreshToken = refreshTokenService
            .findByToken(oldToken)
            .orElseThrow(() -> new GlobalException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));

        refreshToken.setToken(UUID.randomUUID() + "-" + UUID.randomUUID());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpiration));

        return refreshTokenService.save(refreshToken);
    }

    public void revokeRefreshTokenForUser(User user) {
        refreshTokenService.deleteByUser(user);
    }
}
