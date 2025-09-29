package com.budgetmanager.bm.controllers;

import static org.springframework.http.ResponseEntity.ok;

import com.budgetmanager.bm.domain.dtos.TransactionDto;
import com.budgetmanager.bm.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
