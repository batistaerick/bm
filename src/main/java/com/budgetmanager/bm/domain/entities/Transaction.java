package com.budgetmanager.bm.domain.entities;

import com.budgetmanager.bm.enums.RepeatInterval;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Table(name = "t_transaction")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private String notes;

    @NotNull
    private BigDecimal totalValue;

    @Max(value = 500, message = "Value cannot exceed 500")
    @Min(value = 2, message = "Minimum value of 0")
    private Integer installmentNumbers;

    @OneToMany(
        mappedBy = "transaction",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Installment> installments;

    @Enumerated(EnumType.STRING)
    private RepeatInterval repeats;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
