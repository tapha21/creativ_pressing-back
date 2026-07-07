package com.creativpressing.api.dto.response;

import java.util.UUID;

public record AuthResponse(String token, UUID shopId, String shopName, UUID userId, String userName, String email,
        String role, String subscriptionPlan, String subscriptionStatus, String trialEndsAt,
        String subscriptionEndsAt, String logoUrl, Boolean active) {
}
