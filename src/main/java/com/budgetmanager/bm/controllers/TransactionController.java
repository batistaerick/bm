package com.budgetmanager.bm.controllers;

import static org.springframework.http.ResponseEntity.ok;

import com.budgetmanager.bm.domain.dtos.TransactionDto;
import com.budgetmanager.bm.domain.entities.Transaction;
import com.budgetmanager.bm.enums.TransactionType;
import com.budgetmanager.bm.services.TransactionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/type/{transactionType}")
    public ResponseEntity<List<Transaction>> findByTransactionType(
        @PathVariable TransactionType transactionType
    ) {
        return ok(service.findByTransactionType(transactionType));
    }
}
