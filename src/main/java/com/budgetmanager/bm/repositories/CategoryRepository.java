package com.budgetmanager.bm.repositories;

import com.budgetmanager.bm.domain.entities.Category;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, UUID> {}
