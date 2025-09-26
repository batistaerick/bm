package com.budgetmanager.bm.services;

import com.budgetmanager.bm.converters.UserConverter;
import com.budgetmanager.bm.domain.dtos.UserDto;
import com.budgetmanager.bm.domain.entities.User;
import com.budgetmanager.bm.enums.UserRole;
import com.budgetmanager.bm.exceptions.GlobalException;
import com.budgetmanager.bm.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final RoleService roleService;

    public User save(UserDto userDto) {
        if (
            !Pattern
                .compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&.])[A-Za-z\\d@$!%*?&.]{8,20}$")
                .matcher(userDto.password())
                .matches()
        ) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "Password must be 8-20 characters, include uppercase, lowercase, digit, and special character");
        }
        User user = UserConverter.dtoToEntity(userDto);

        user.setRoles(Collections.singleton(roleService.findByRoleName(UserRole.USER)));
        user.setPassword(encoder.encode(user.getPassword()));

        return repository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public User findById(UUID id) {
        return repository
            .findById(id)
            .orElseThrow(
                () -> new GlobalException(
                    HttpStatus.NOT_FOUND,
                    "User not found for {}",
                    id
                )
            );
    }

    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
}
