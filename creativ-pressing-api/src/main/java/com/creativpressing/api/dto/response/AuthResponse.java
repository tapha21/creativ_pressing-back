package com.creativpressing.api.dto.response;

import java.util.UUID;

public record AuthResponse(UUID shopId, String shopName, UUID userId, String userName, String email, String role) {
}
