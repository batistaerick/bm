package com.budgetmanager.bm.repositories;

import com.budgetmanager.bm.domain.entities.Installment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InstallmentRepository extends JpaRepository<Installment, UUID> {
}
