package com.creativpressing.api.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record DashboardSummaryResponse(BigDecimal monthlyRevenue, long depositedOrders, long activeClients,
        BigDecimal monthlyExpenses, String revenueTrend, String ordersTrend, String clientsTrend, String expensesTrend,
        List<RevenuePointResponse> revenueChart, List<OrdersPointResponse> ordersChart,
        List<OrderResponse> recentOrders, long todayDeposits, long readyForPickup, long overdueOrders) {
}
