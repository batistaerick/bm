package com.budgetmanager.bm.repositories;

import com.budgetmanager.bm.domain.entities.UserImage;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserImageRepository extends JpaRepository<UserImage, UUID> {
    Optional<UserImage> findByUserEmail(String email);
}
