package com.budgetmanager.bm.repositories;

import com.budgetmanager.bm.domain.entities.Installment;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstallmentRepository
    extends JpaRepository<Installment, UUID> {}
