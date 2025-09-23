package com.budgetmanager.bm.controllers;

import com.budgetmanager.bm.converters.UserConverter;
import com.budgetmanager.bm.domain.dtos.UserDto;
import com.budgetmanager.bm.domain.entities.User;
import com.budgetmanager.bm.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    public ResponseEntity<UserDto> save(@RequestBody UserDto userDto) {
        User user = service.save(userDto);

        URI uri = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(user.getId())
            .toUri();

        return created(uri).body(UserConverter.entityToDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable UUID id) {
        return ok(UserConverter.entityToDto(service.findById(id)));
    }
}
