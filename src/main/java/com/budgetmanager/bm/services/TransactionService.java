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
    private final CategoryService categoryService;

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

        validateTransaction(updatedTransaction);

        Transaction transaction = TransactionConverter.dtoToEntity(
            normalize(updatedTransaction)
        );
        transaction.setCategory(
            categoryService
                .findById(updatedTransaction.category().id())
                .orElseThrow(() ->
                    new GlobalException(
                        HttpStatus.NOT_FOUND,
                        "Category not found for {}",
                        updatedTransaction.category().id()
                    )
                )
        );
        transaction.setUser(user);

        return repository.save(transaction);
    }

    @Transactional
    public Transaction save(TransactionDto dto) {
        validateTransaction(dto);
        User user = userService
            .getCurrentUser()
            .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        Transaction newTransaction = TransactionConverter.dtoToEntity(
            normalize(dto)
        );

        newTransaction.setUser(user);
        newTransaction.setCategory(
            categoryService
                .findById(dto.category().id())
                .orElseThrow(() ->
                    new GlobalException(
                        HttpStatus.NOT_FOUND,
                        "Category not found for {}",
                        dto.category().id()
                    )
                )
        );
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

    private void validateTransaction(TransactionDto dto) {
        if (dto.category() == null || dto.category().id() == null) {
            throw new GlobalException(
                HttpStatus.BAD_REQUEST,
                "Category id is required"
            );
        }
        if (
            dto.installmentNumbers() != null &&
            normalize(dto).repeats() != RepeatInterval.NONE
        ) {
            throw new GlobalException(
                HttpStatus.BAD_REQUEST,
                "Transaction cannot repeat and have installments"
            );
        }
    }

    private TransactionDto normalize(TransactionDto dto) {
        return TransactionDto.builder()
            .id(dto.id())
            .category(dto.category())
            .notes(dto.notes())
            .totalValue(dto.totalValue())
            .installmentNumbers(dto.installmentNumbers())
            .installments(null)
            .repeats(
                dto.repeats() == null ? RepeatInterval.NONE : dto.repeats()
            )
            .date(dto.date())
            .build();
    }
}
