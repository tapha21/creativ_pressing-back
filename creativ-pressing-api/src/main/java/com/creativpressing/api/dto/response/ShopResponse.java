package com.creativpressing.api.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ShopResponse(UUID id, String name, String ownerName, String phone, String city, String address,
        Boolean active, LocalDateTime createdAt) {
}
