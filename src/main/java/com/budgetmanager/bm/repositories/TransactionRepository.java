package com.budgetmanager.bm.repositories;

import com.budgetmanager.bm.domain.entities.Transaction;
import com.budgetmanager.bm.enums.TransactionType;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository
    extends JpaRepository<Transaction, UUID> {
    @Query(
        """
            SELECT DISTINCT t
            FROM Transaction t
            JOIN FETCH t.category c
            LEFT JOIN t.installments i
            WHERE t.user.id = :userId
              AND t.category.transactionType = :type
              AND (
                      (
                          t.installmentNumbers IS NULL
                          AND (
                              t.repeats <> 'NONE' OR
                              (t.date BETWEEN :startDate AND :endDate)
                          )
                      )
                      OR
                      (
                          t.installmentNumbers IS NOT NULL
                          AND i.dueDate BETWEEN :startDate AND :endDate
                      )
              )
        """
    )
    List<Transaction> findAllWithCategoryByUserAndTypeAndDateBetween(
        @Param("userId") UUID userId,
        @Param("type") TransactionType type,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}
