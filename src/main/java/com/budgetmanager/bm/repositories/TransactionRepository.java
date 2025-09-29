package com.budgetmanager.bm.repositories;

import com.budgetmanager.bm.domain.entities.Transaction;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository
    extends JpaRepository<Transaction, UUID> {}
