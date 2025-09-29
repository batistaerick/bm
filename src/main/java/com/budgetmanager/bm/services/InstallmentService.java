package com.budgetmanager.bm.services;

import com.budgetmanager.bm.converters.InstallmentConverter;
import com.budgetmanager.bm.domain.dtos.InstallmentDto;
import com.budgetmanager.bm.domain.entities.Installment;
import com.budgetmanager.bm.repositories.InstallmentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstallmentService {

    private final InstallmentRepository repository;

    public Installment save(InstallmentDto dto) {
        return repository.save(InstallmentConverter.dtoToEntity(dto));
    }

    public List<Installment> saveAll(List<Installment> installments) {
        return repository.saveAll(installments);
    }
}
