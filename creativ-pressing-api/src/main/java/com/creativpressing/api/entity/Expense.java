package com.creativpressing.api.entity;

import com.creativpressing.api.enums.ExpenseCategory;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("expenses")
public class Expense extends BaseEntity {
    private ExpenseCategory category;
    private String description;
    private BigDecimal amount;
    private LocalDate date;
    private UUID shopId;
}
