package com.budgetmanager.bm.controllers;

import com.budgetmanager.bm.converters.RoleConverter;
import com.budgetmanager.bm.domain.dtos.RoleDto;
import com.budgetmanager.bm.domain.entities.Role;
import com.budgetmanager.bm.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static org.springframework.http.ResponseEntity.created;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService service;

    @PostMapping
    public ResponseEntity<RoleDto> save(@RequestBody RoleDto roleDto) {
        Role role = service.save(roleDto);

        URI uri = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(role.getId())
            .toUri();

        return created(uri).body(RoleConverter.entityToDto(role));
    }
}
