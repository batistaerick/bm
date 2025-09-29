package com.budgetmanager.bm.controllers;

import static org.springframework.http.ResponseEntity.ok;

import com.budgetmanager.bm.domain.dtos.CategoryDto;
import com.budgetmanager.bm.domain.entities.Category;
import com.budgetmanager.bm.enums.TransactionType;
import com.budgetmanager.bm.services.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @PostMapping
    public ResponseEntity<Category> save(@RequestBody CategoryDto dto) {
        return ok(service.save(dto));
    }

    @GetMapping("/type/{transactionType}")
    public ResponseEntity<List<Category>> findByTransactionType(
        @PathVariable TransactionType transactionType
    ) {
        return ok(service.findByTransactionType(transactionType));
    }
}
