package com.budgetmanager.bm.controllers;

import static org.springframework.http.ResponseEntity.created;

import com.budgetmanager.bm.converters.RoleConverter;
import com.budgetmanager.bm.domain.dtos.RoleDto;
import com.budgetmanager.bm.domain.entities.Role;
import com.budgetmanager.bm.services.RoleService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    private final RoleService service;

    @PostMapping
    public ResponseEntity<RoleDto> save(@Valid @RequestBody RoleDto roleDto) {
        Role role = service.save(roleDto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(role.getId())
            .toUri();

        return created(uri).body(RoleConverter.entityToDto(role));
    }
}
