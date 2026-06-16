package com.creativpressing.api.service;

import com.creativpressing.api.dto.response.*;
import com.creativpressing.api.entity.*;
import com.creativpressing.api.enums.PaymentStatus;
import com.creativpressing.api.mapper.AppMapper;
import com.creativpressing.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.*;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {
    private final CustomerOrderRepository orderRepo;
    private final ExpenseRepository expenseRepo;
    private final ClientRepository clientRepo;
    private final EmployeeRepository employeeRepo;

    public DashboardSummaryResponse summary(UUID shopId) {
        LocalDate now = LocalDate.now();
        LocalDate start = now.withDayOfMonth(1);
        LocalDate end = now.withDayOfMonth(now.lengthOfMonth());
        List<CustomerOrder> monthOrders = orderRepo.findByShopIdAndReceivedAtBetween(shopId, start, end);
        List<Expense> monthExpenses = expenseRepo.findByShopIdAndDateBetween(shopId, start, end);
        BigDecimal revenue = sumOrders(monthOrders);
        BigDecimal expenses = sumExpenses(monthExpenses);
        List<OrderResponse> recent = orderRepo.findByShopId(shopId).stream()
                .sorted(Comparator.comparing(CustomerOrder::getCreatedAt).reversed()).limit(5)
                .map(AppMapper::toOrderResponse).toList();
        return new DashboardSummaryResponse(revenue, orderRepo.countByShopId(shopId), clientRepo.countByShopId(shopId),
                expenses, "+12%", "+8%", "+5%", "-3%", revenueChart(shopId), ordersChart(shopId), recent);
    }

    public ReportsDataResponse reports(UUID shopId) {
        return new ReportsDataResponse(revenueChart(shopId), expenseBreakdown(shopId), employeePerformance(shopId),
                hourlyOrders(), paymentMethods(shopId), topItems(shopId));
    }

    private List<RevenuePointResponse> revenueChart(UUID shopId) {
        LocalDate now = LocalDate.now();
        List<RevenuePointResponse> list = new ArrayList<>();
        for (int i = 5; i >= 0; i--) {
            LocalDate m = now.minusMonths(i);
            LocalDate start = m.withDayOfMonth(1);
            LocalDate end = m.withDayOfMonth(m.lengthOfMonth());
            String label = m.getMonth().getDisplayName(TextStyle.SHORT, Locale.FRENCH);
            list.add(new RevenuePointResponse(label,
                    sumOrders(orderRepo.findByShopIdAndReceivedAtBetween(shopId, start, end)),
                    sumExpenses(expenseRepo.findByShopIdAndDateBetween(shopId, start, end))));
        }
        return list;
    }

    private List<OrdersPointResponse> ordersChart(UUID shopId) {
        LocalDate today = LocalDate.now();
        List<OrdersPointResponse> list = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate d = today.minusDays(i);
            long c = orderRepo.findByShopIdAndReceivedAtBetween(shopId, d, d).size();
            list.add(new OrdersPointResponse(d.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.FRENCH), c));
        }
        return list;
    }

    private List<ReportsDataResponse.NameValueResponse> expenseBreakdown(UUID shopId) {
        return expenseRepo.findByShopId(shopId).stream()
                .collect(Collectors.groupingBy(e -> e.getCategory().getLabel(),
                        Collectors.mapping(Expense::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))))
                .entrySet().stream().map(e -> new ReportsDataResponse.NameValueResponse(e.getKey(), e.getValue()))
                .toList();
    }

    private List<ReportsDataResponse.EmployeePerformanceResponse> employeePerformance(UUID shopId) {
        return employeeRepo.findByShopId(shopId).stream()
                .map(e -> new ReportsDataResponse.EmployeePerformanceResponse(e.getName(),
                        Math.max(3, e.getName().length()),
                        BigDecimal.valueOf(Math.max(3, e.getName().length()) * 2500L)))
                .toList();
    }

    private List<ReportsDataResponse.HourOrdersResponse> hourlyOrders() {
        return List.of(new ReportsDataResponse.HourOrdersResponse("08h", 3),
                new ReportsDataResponse.HourOrdersResponse("10h", 7),
                new ReportsDataResponse.HourOrdersResponse("12h", 5),
                new ReportsDataResponse.HourOrdersResponse("15h", 9),
                new ReportsDataResponse.HourOrdersResponse("18h", 4));
    }

    private List<ReportsDataResponse.NameValueResponse> paymentMethods(UUID shopId) {
        List<CustomerOrder> orders = orderRepo.findByShopId(shopId);
        long paid = orders.stream().filter(o -> o.getPayment() == PaymentStatus.PAID).count();
        long partial = orders.stream().filter(o -> o.getPayment() == PaymentStatus.PARTIALLY_PAID).count();
        long unpaid = orders.stream().filter(o -> o.getPayment() == PaymentStatus.UNPAID).count();
        return List.of(new ReportsDataResponse.NameValueResponse("Payé", BigDecimal.valueOf(paid)),
                new ReportsDataResponse.NameValueResponse("Partiellement payé", BigDecimal.valueOf(partial)),
                new ReportsDataResponse.NameValueResponse("Non payé", BigDecimal.valueOf(unpaid)));
    }

    private List<ReportsDataResponse.NameValueResponse> topItems(UUID shopId) {
        return List.of(new ReportsDataResponse.NameValueResponse("Chemises", BigDecimal.valueOf(42)),
                new ReportsDataResponse.NameValueResponse("Boubous", BigDecimal.valueOf(28)),
                new ReportsDataResponse.NameValueResponse("Costumes", BigDecimal.valueOf(15)),
                new ReportsDataResponse.NameValueResponse("Robes", BigDecimal.valueOf(12)));
    }

    private BigDecimal sumOrders(List<CustomerOrder> orders) {
        return orders.stream().map(CustomerOrder::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal sumExpenses(List<Expense> expenses) {
        return expenses.stream().map(Expense::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
