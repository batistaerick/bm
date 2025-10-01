package com.budgetmanager.bm.controllers;

import com.budgetmanager.bm.domain.dtos.TransactionDto;
import com.budgetmanager.bm.domain.entities.Transaction;
import com.budgetmanager.bm.enums.TransactionType;
import com.budgetmanager.bm.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @PostMapping
    public ResponseEntity<TransactionDto> save(
        @RequestBody TransactionDto dto
    ) {
        return ok(service.save(dto));
    }

    @GetMapping
    public ResponseEntity<
        List<Transaction>
        > findByTransactionTypeAndDateBetween(
        @RequestParam TransactionType transactionType,
        @RequestParam @DateTimeFormat(
            pattern = "yyyy/MM/dd"
        ) LocalDate startDate,
        @RequestParam @DateTimeFormat(pattern = "yyyy/MM/dd") LocalDate endDate
    ) {
        return ok(
            service.findByTransactionTypeAndDateBetween(
                transactionType,
                startDate,
                endDate
            )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteById(id);
        return noContent().build();
    }
}
