package com.budgetmanager.bm.controllers;

import static org.springframework.http.ResponseEntity.*;

import com.budgetmanager.bm.converters.SavingConverter;
import com.budgetmanager.bm.domain.dtos.SavingDto;
import com.budgetmanager.bm.domain.entities.Saving;
import com.budgetmanager.bm.services.SavingService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/savings")
@RequiredArgsConstructor
public class SavingController {

    private final SavingService service;

    @GetMapping
    public ResponseEntity<List<SavingDto>> findAll() {
        return ok(
            service
                .findAll()
                .stream()
                .map(SavingConverter::entityToDto)
                .toList()
        );
    }

    @PostMapping
    public ResponseEntity<SavingDto> save(@Valid @RequestBody SavingDto dto) {
        Saving saving = service.save(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(saving.getId())
            .toUri();

        return created(uri).body(SavingConverter.entityToDto(saving));
    }

    @PutMapping
    public ResponseEntity<SavingDto> update(@Valid @RequestBody SavingDto dto) {
        return ok(SavingConverter.entityToDto(service.update(dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteById(id);
        return noContent().build();
    }
}
