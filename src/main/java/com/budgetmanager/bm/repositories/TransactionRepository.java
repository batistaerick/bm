package com.budgetmanager.bm.repositories;

import com.budgetmanager.bm.domain.entities.Transaction;
import com.budgetmanager.bm.enums.TransactionType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository
    extends JpaRepository<Transaction, UUID>
{
    Optional<Transaction> findByIdAndUserId(UUID id, UUID userId);

    @Query(
        value = """
            SELECT t.id
            FROM Transaction t
            WHERE t.user.id = :userId
              AND t.category.transactionType = :type
              AND (
                      (
                          t.installmentNumbers IS NULL
                          AND (
                              (t.repeats <> 'NONE' AND t.date <= :endDate) OR
                              (t.date BETWEEN :startDate AND :endDate)
                          )
                      )
                      OR
                      (
                          t.installmentNumbers IS NOT NULL
                          AND EXISTS (
                              SELECT 1
                              FROM Installment i
                              WHERE i.transaction = t
                                AND i.dueDate BETWEEN :startDate AND :endDate
                          )
                      )
              )
            ORDER BY
              CASE WHEN :sortKey = 'category' AND :sortOrder = 'asc' THEN LOWER(t.category.name) END ASC,
              CASE WHEN :sortKey = 'category' AND :sortOrder = 'desc' THEN LOWER(t.category.name) END DESC,
              CASE WHEN :sortKey = 'notes' AND :sortOrder = 'asc' THEN LOWER(COALESCE(t.notes, '')) END ASC,
              CASE WHEN :sortKey = 'notes' AND :sortOrder = 'desc' THEN LOWER(COALESCE(t.notes, '')) END DESC,
              CASE WHEN :sortKey = 'date' AND :sortOrder = 'asc' THEN t.date END ASC,
              CASE WHEN :sortKey = 'date' AND :sortOrder = 'desc' THEN t.date END DESC,
              CASE
                  WHEN :sortKey = 'value'
                    AND :sortOrder = 'asc'
                    AND t.installmentNumbers IS NULL
                  THEN t.totalValue
                  WHEN :sortKey = 'value'
                    AND :sortOrder = 'asc'
                    AND t.installmentNumbers IS NOT NULL
                  THEN t.totalValue / t.installmentNumbers
              END ASC,
              CASE
                  WHEN :sortKey = 'value'
                    AND :sortOrder = 'desc'
                    AND t.installmentNumbers IS NULL
                  THEN t.totalValue
                  WHEN :sortKey = 'value'
                    AND :sortOrder = 'desc'
                    AND t.installmentNumbers IS NOT NULL
                  THEN t.totalValue / t.installmentNumbers
              END DESC,
              t.id DESC
        """,
        countQuery = """
            SELECT COUNT(t.id)
            FROM Transaction t
            WHERE t.user.id = :userId
              AND t.category.transactionType = :type
              AND (
                      (
                          t.installmentNumbers IS NULL
                          AND (
                              (t.repeats <> 'NONE' AND t.date <= :endDate) OR
                              (t.date BETWEEN :startDate AND :endDate)
                          )
                      )
                      OR
                      (
                          t.installmentNumbers IS NOT NULL
                          AND EXISTS (
                              SELECT 1
                              FROM Installment i
                              WHERE i.transaction = t
                                AND i.dueDate BETWEEN :startDate AND :endDate
                          )
                      )
              )
        """
    )
    Page<UUID> findIdsByUserAndTypeAndDateBetween(
        @Param("userId") UUID userId,
        @Param("type") TransactionType type,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("sortKey") String sortKey,
        @Param("sortOrder") String sortOrder,
        Pageable pageable
    );

    @Query(
        """
            SELECT DISTINCT t
            FROM Transaction t
            JOIN FETCH t.category c
            LEFT JOIN FETCH t.installments i
            WHERE t.id IN :ids
        """
    )
    List<Transaction> findAllWithCategoryAndInstallmentsByIdIn(
        @Param("ids") List<UUID> ids
    );
}
