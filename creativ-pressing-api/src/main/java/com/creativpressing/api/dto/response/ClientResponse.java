package com.creativpressing.api.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ClientResponse(UUID id, String name, String phone, String address, String city, LocalDateTime createdAt,
        long totalOrders) {
}
