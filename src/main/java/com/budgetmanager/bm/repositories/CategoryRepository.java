package com.budgetmanager.bm.repositories;

import com.budgetmanager.bm.domain.entities.Category;
import com.budgetmanager.bm.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByTransactionType(TransactionType transactionType);
}
