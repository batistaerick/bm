package com.budgetmanager.bm.repositories;

import com.budgetmanager.bm.domain.entities.Saving;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingRepository extends JpaRepository<Saving, UUID> {
    List<Saving> findAllByUserIdOrderByLocationAscNameAsc(UUID userId);

    Optional<Saving> findByIdAndUserId(UUID id, UUID userId);
}
