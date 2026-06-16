package com.creativpressing.api.dto.request;

import com.creativpressing.api.enums.ExpenseCategory;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ExpenseRequest(@NotNull UUID shopId, @NotNull ExpenseCategory category, @NotBlank String description,
                @NotNull @PositiveOrZero BigDecimal amount, @NotNull LocalDate date) {
}
