package com.budgetmanager.bm.services;

import com.budgetmanager.bm.converters.UserConverter;
import com.budgetmanager.bm.domain.dtos.UserDto;
import com.budgetmanager.bm.domain.entities.User;
import com.budgetmanager.bm.enums.UserRole;
import com.budgetmanager.bm.exceptions.GlobalException;
import com.budgetmanager.bm.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final RoleService roleService;

    public User save(UserDto userDto) {
        User user = UserConverter.dtoToEntity(userDto);

        user.setRoles(Collections.singleton(roleService.findByRoleName(UserRole.USER)));
        user.setPassword(encoder.encode(user.getPassword()));

        return repository.save(user);
    }

    public User findByEmail(String email) {
        return repository
            .findByEmail(email)
            .orElseThrow(
                () -> new GlobalException(
                    HttpStatus.NOT_FOUND,
                    "User not found for {}",
                    email
                )
            );
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository
            .findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found for email " + username));
    }
}
