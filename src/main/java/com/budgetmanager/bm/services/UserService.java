package com.budgetmanager.bm.services;

import com.budgetmanager.bm.converters.UserConverter;
import com.budgetmanager.bm.domain.dtos.UserDto;
import com.budgetmanager.bm.domain.entities.User;
import com.budgetmanager.bm.enums.UserRole;
import com.budgetmanager.bm.exceptions.GlobalException;
import com.budgetmanager.bm.repositories.UserRepository;
import com.budgetmanager.bm.utils.Checkers;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final RoleService roleService;

    public User save(UserDto userDto) {
        repository
            .findByEmail(userDto.email())
            .ifPresent(existing -> {
                throw new GlobalException(
                    HttpStatus.CONFLICT,
                    "Email already registered"
                );
            });
        if (!Checkers.isPasswordCorrect(userDto.password())) {
            throw new GlobalException(
                HttpStatus.BAD_REQUEST,
                "Password must be 8-20 characters, include uppercase, lowercase, digit, and special character"
            );
        }
        User user = UserConverter.dtoToEntity(userDto);

        user.setRoles(
            Collections.singleton(
                roleService.findByRoleName(UserRole.ROLE_USER)
            )
        );
        user.setPassword(encoder.encode(user.getPassword()));

        return repository.save(user);
    }

    public void updateUser(UserDto dto) {
        User updatedUser = getCurrentUser().orElseThrow(() ->
            new UsernameNotFoundException("User not found")
        );
        if (dto.name() != null && !dto.name().isBlank()) {
            updatedUser.setName(dto.name());
        }
        if (dto.password() != null && !dto.password().isBlank()) {
            if (!Checkers.isPasswordCorrect(dto.password())) {
                throw new GlobalException(
                    HttpStatus.BAD_REQUEST,
                    "Password must be 8-20 characters, include uppercase, lowercase, digit, and special character"
                );
            }
            if (encoder.matches(dto.password(), updatedUser.getPassword())) {
                throw new GlobalException(
                    HttpStatus.BAD_REQUEST,
                    "Password must be different from before"
                );
            }
            updatedUser.setPassword(encoder.encode(dto.password()));
        }
        repository.save(updatedUser);
    }

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public User findById(UUID id) {
        return repository
            .findById(id)
            .orElseThrow(() ->
                new GlobalException(
                    HttpStatus.NOT_FOUND,
                    "User not found for {}",
                    id
                )
            );
    }

    public Optional<User> getCurrentUser() {
        return repository.findByEmail(getCurrentUsername());
    }

    public String getCurrentUsername() {
        Authentication auth =
            SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new UsernameNotFoundException("User not authenticated");
        }
        return auth.getName();
    }
}
