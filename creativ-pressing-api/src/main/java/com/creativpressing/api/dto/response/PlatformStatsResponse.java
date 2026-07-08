package com.creativpressing.api.dto.response;

import java.math.BigDecimal;

public record PlatformStatsResponse(long totalShops, long totalClients, long totalOrders, long totalEmployees,
        BigDecimal monthlyRevenue) {
}
