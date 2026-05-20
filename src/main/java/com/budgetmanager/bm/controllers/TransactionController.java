package com.budgetmanager.bm.controllers;

import static org.springframework.http.ResponseEntity.*;

import com.budgetmanager.bm.converters.TransactionConverter;
import com.budgetmanager.bm.domain.dtos.TransactionDto;
import com.budgetmanager.bm.domain.entities.Transaction;
import com.budgetmanager.bm.enums.TransactionType;
import com.budgetmanager.bm.services.TransactionService;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @PostMapping
    public ResponseEntity<TransactionDto> save(
        @Valid @RequestBody TransactionDto dto
    ) {
        Transaction transaction = service.save(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(transaction.getId())
            .toUri();

        return created(uri).body(TransactionConverter.entityToDto(transaction));
    }

    @GetMapping
    public ResponseEntity<
        Page<TransactionDto>
    > findByTransactionTypeAndDateBetween(
        @RequestParam TransactionType transactionType,
        @RequestParam @DateTimeFormat(
            pattern = "yyyy/MM/dd"
        ) LocalDate startDate,
        @RequestParam @DateTimeFormat(pattern = "yyyy/MM/dd") LocalDate endDate,
        @RequestParam(defaultValue = "date") String sortKey,
        @RequestParam(defaultValue = "asc") String sortOrder,
        @PageableDefault(size = 30) Pageable pageable
    ) {
        return ok(
            service
                .findByTransactionTypeAndDateBetween(
                    transactionType,
                    startDate,
                    endDate,
                    sortKey,
                    sortOrder,
                    pageable
                )
                .map(TransactionConverter::entityToDto)
        );
    }

    @PutMapping
    public ResponseEntity<TransactionDto> update(
        @Valid @RequestBody TransactionDto dto
    ) {
        return ok(TransactionConverter.entityToDto(service.update(dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteById(id);
        return noContent().build();
    }
}
