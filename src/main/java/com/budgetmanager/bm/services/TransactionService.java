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
import java.util.ArrayList;
import java.util.List;
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

    public List<Transaction> findByTransactionType(
        TransactionType transactionType
    ) {
        return repository.findAllWithCategoryByUserAndType(
            userService
                .getCurrentUser()
                .orElseThrow(() ->
                    new UsernameNotFoundException("User not found")
                )
                .getId(),
            transactionType
        );
    }

    @Transactional
    public TransactionDto save(TransactionDto dto) {
        User user = userService
            .getCurrentUser()
            .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        Transaction newTransaction = TransactionConverter.dtoToEntity(dto);

        newTransaction.setUser(user);
        newTransaction = repository.save(newTransaction);

        if (dto.installmentNumbers() != null) {
            if (!dto.repeats().equals(RepeatInterval.NONE)) {
                throw new GlobalException(
                    HttpStatus.BAD_GATEWAY,
                    "Transaction cannot have installments and repeat interval at the same time"
                );
            }
            handleInstallments(newTransaction);
        }
        return TransactionConverter.entityToDto(newTransaction);
    }

    public void handleInstallments(Transaction transaction) {
        List<Installment> installments = new ArrayList<>();
        BigDecimal installmentAmount = calculateInstallmentAmount(transaction);

        for (int i = 1; i <= transaction.getInstallmentNumbers(); i++) {
            installments.add(
                Installment.builder()
                    .transaction(transaction)
                    .installmentNumber(i)
                    .totalInstallments(transaction.getInstallmentNumbers())
                    .amount(installmentAmount)
                    .dueDate(transaction.getDate().plusMonths(i - 1))
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
                RoundingMode.HALF_UP
            );
    }
}
