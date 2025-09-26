package com.budgetmanager.bm.services;

import com.budgetmanager.bm.domain.entities.RefreshToken;
import com.budgetmanager.bm.domain.entities.User;
import com.budgetmanager.bm.repositories.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository repository;

    public void deleteByUser(User user) {
        repository.deleteByUser(user);
    }

    public RefreshToken save(RefreshToken refreshToken) {
        return repository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return repository.findByToken(token);
    }
}
