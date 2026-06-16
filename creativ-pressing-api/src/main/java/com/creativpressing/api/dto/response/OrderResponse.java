package com.creativpressing.api.dto.response;

import com.creativpressing.api.enums.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderResponse(UUID id, UUID clientId, String clientName, String clientPhone, String items,
        BigDecimal amount, OrderStatus status, PaymentStatus payment, LocalDate receivedAt, LocalDate deliveryAt,
        String attachmentName, LocalDateTime createdAt) {
}
