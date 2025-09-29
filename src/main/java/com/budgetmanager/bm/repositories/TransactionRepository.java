package com.budgetmanager.bm.repositories;

import com.budgetmanager.bm.domain.entities.Transaction;
import com.budgetmanager.bm.enums.TransactionType;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository
    extends JpaRepository<Transaction, UUID> {
    @Query(
        """
            SELECT t
            FROM Transaction t
            JOIN FETCH t.category c
            WHERE t.user.id = :userId AND t.category.transactionType = :type
        """
    )
    List<Transaction> findAllWithCategoryByUserAndType(
        @Param("userId") UUID userId,
        @Param("type") TransactionType type
    );
}
