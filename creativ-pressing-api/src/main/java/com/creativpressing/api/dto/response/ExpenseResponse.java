package com.creativpressing.api.dto.response;

import com.creativpressing.api.enums.ExpenseCategory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ExpenseResponse(UUID id, ExpenseCategory category, String description, BigDecimal amount,
        LocalDate date) {
}
