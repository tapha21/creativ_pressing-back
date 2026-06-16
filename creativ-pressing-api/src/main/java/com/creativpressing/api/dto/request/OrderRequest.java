package com.creativpressing.api.dto.request;

import com.creativpressing.api.enums.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record OrderRequest(@NotNull UUID shopId, UUID clientId, @NotBlank String clientName, String clientPhone,
        @NotBlank String items, @NotNull @PositiveOrZero BigDecimal amount, @NotNull OrderStatus status,
        @NotNull PaymentStatus payment, @NotNull LocalDate receivedAt, @NotNull LocalDate deliveryAt,
        String attachmentName) {
}
