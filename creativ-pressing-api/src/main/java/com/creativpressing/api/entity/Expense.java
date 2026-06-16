package com.creativpressing.api.entity;

import com.creativpressing.api.enums.ExpenseCategory;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "expenses")
public class Expense extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private ExpenseCategory category;
    @Column(nullable = false, length = 255)
    private String description;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;
    @Column(nullable = false)
    private LocalDate date;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", nullable = false)
    private PressingShop shop;
}
