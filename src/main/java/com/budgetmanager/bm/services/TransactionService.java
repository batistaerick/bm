package com.budgetmanager.bm.services;

import com.budgetmanager.bm.converters.TransactionConverter;
import com.budgetmanager.bm.domain.dtos.TransactionDto;
import com.budgetmanager.bm.domain.entities.Installment;
import com.budgetmanager.bm.domain.entities.Transaction;
import com.budgetmanager.bm.domain.entities.User;
import com.budgetmanager.bm.enums.RepeatInterval;
import com.budgetmanager.bm.enums.TransactionType;
import com.budgetmanager.bm.exceptions.GlobalException;
import com.budgetmanager.bm.repositories.TransactionRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;
    private final InstallmentService installmentService;
    private final UserService userService;

    public List<Transaction> findByTransactionTypeAndDateBetween(
        TransactionType transactionType,
        LocalDate startDate,
        LocalDate endDate
    ) {
        UUID id = userService
            .getCurrentUser()
            .orElseThrow(() -> new UsernameNotFoundException("User not found"))
            .getId();

        return repository.findAllWithCategoryByUserAndTypeAndDateBetween(
            id,
            transactionType,
            startDate,
            endDate
        );
    }

    public void deleteById(UUID id) {
        User user = userService
            .getCurrentUser()
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Transaction transaction = repository
            .findByIdAndUserId(id, user.getId())
            .orElseThrow(() ->
                new GlobalException(
                    HttpStatus.NOT_FOUND,
                    "Transaction not found for {}",
                    id
                )
            );

        repository.delete(transaction);
    }

    @Transactional
    public Transaction update(TransactionDto updatedTransaction) {
        User user = userService
            .getCurrentUser()
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (updatedTransaction.id() == null) {
            throw new GlobalException(
                HttpStatus.BAD_REQUEST,
                "Transaction id is required"
            );
        }

        repository
            .findByIdAndUserId(updatedTransaction.id(), user.getId())
            .orElseThrow(() ->
                new GlobalException(
                    HttpStatus.NOT_FOUND,
                    "Transaction not found for {}",
                    updatedTransaction.id()
                )
            );

        Transaction transaction = TransactionConverter.dtoToEntity(
            updatedTransaction
        );
        transaction.setUser(user);

        return repository.save(transaction);
    }

    @Transactional
    public Transaction save(TransactionDto dto) {
        if (
            dto.installmentNumbers() != null &&
            !dto.repeats().equals(RepeatInterval.NONE)
        ) {
            throw new GlobalException(
                HttpStatus.BAD_REQUEST,
                "Transaction cannot repeat and have installments"
            );
        }
        User user = userService
            .getCurrentUser()
            .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        Transaction newTransaction = TransactionConverter.dtoToEntity(dto);

        newTransaction.setUser(user);
        newTransaction = repository.save(newTransaction);

        if (dto.installmentNumbers() != null) {
            handleInstallments(newTransaction);
        }
        return newTransaction;
    }

    private void handleInstallments(Transaction transaction) {
        List<Installment> installments = new ArrayList<>();
        BigDecimal installmentAmount = calculateInstallmentAmount(transaction);

        for (int i = 1; i <= transaction.getInstallmentNumbers(); i++) {
            installments.add(
                Installment.builder()
                    .transaction(transaction)
                    .installmentNumber(i)
                    .totalInstallments(transaction.getInstallmentNumbers())
                    .amount(installmentAmount)
                    .dueDate(transaction.getDate().plusMonths((long) i - 1))
                    .build()
            );
        }
        transaction.setInstallments(installmentService.saveAll(installments));
    }

    private BigDecimal calculateInstallmentAmount(Transaction transaction) {
        return transaction
            .getTotalValue()
            .divide(
                BigDecimal.valueOf(transaction.getInstallmentNumbers()),
                2,
                RoundingMode.UP
            );
    }
}
