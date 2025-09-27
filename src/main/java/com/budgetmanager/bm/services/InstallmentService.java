package com.budgetmanager.bm.services;

import com.budgetmanager.bm.repositories.InstallmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstallmentService {
    private final InstallmentRepository repository;
}
