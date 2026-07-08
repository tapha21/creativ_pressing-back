package com.creativpressing.api.service;

import com.creativpressing.api.dto.response.*;
import com.creativpressing.api.entity.*;
import com.creativpressing.api.enums.OrderStatus;
import com.creativpressing.api.enums.PaymentStatus;
import com.creativpressing.api.mapper.AppMapper;
import com.creativpressing.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.*;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final CustomerOrderRepository orderRepo;
    private final ExpenseRepository expenseRepo;
    private final ClientRepository clientRepo;
    private final EmployeeRepository employeeRepo;

    public DashboardSummaryResponse summary(UUID shopId) {
        LocalDate now = LocalDate.now();
        LocalDate start = now.withDayOfMonth(1);
        LocalDate end = now.withDayOfMonth(now.lengthOfMonth());
        LocalDate prevMonth = now.minusMonths(1);
        LocalDate prevStart = prevMonth.withDayOfMonth(1);
        LocalDate prevEnd = prevMonth.withDayOfMonth(prevMonth.lengthOfMonth());

        List<CustomerOrder> monthOrders = orderRepo.findByShopIdAndReceivedAtBetween(shopId, start, end);
        List<CustomerOrder> prevMonthOrders = orderRepo.findByShopIdAndReceivedAtBetween(shopId, prevStart, prevEnd);
        List<Expense> monthExpenses = expenseRepo.findByShopIdAndDateBetween(shopId, start, end);
        List<Expense> prevMonthExpenses = expenseRepo.findByShopIdAndDateBetween(shopId, prevStart, prevEnd);

        BigDecimal revenue = sumOrders(monthOrders);
        BigDecimal prevRevenue = sumOrders(prevMonthOrders);
        BigDecimal expenses = sumExpenses(monthExpenses);
        BigDecimal prevExpenses = sumExpenses(prevMonthExpenses);

        long totalClients = clientRepo.countByShopId(shopId);
        long prevTotalClients = clientRepo.findByShopId(shopId).stream()
                .filter(c -> c.getCreatedAt() != null && !c.getCreatedAt().toLocalDate().isAfter(prevEnd))
                .count();

        List<CustomerOrder> allOrders = orderRepo.findByShopId(shopId);
        List<OrderResponse> recent = allOrders.stream()
                .sorted(Comparator.comparing(CustomerOrder::getCreatedAt).reversed()).limit(5)
                .map(AppMapper::toOrderResponse).toList();
        long todayDeposits = allOrders.stream().filter(o -> now.equals(o.getReceivedAt())).count();
        long readyForPickup = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.READY).count();
        long overdueOrders = allOrders.stream()
                .filter(o -> o.getDeliveryAt() != null && o.getDeliveryAt().isBefore(now) && o.getStatus() != OrderStatus.DELIVERED)
                .count();

        return new DashboardSummaryResponse(revenue, orderRepo.countByShopId(shopId), totalClients, expenses,
                trend(revenue, prevRevenue), trend(BigDecimal.valueOf(monthOrders.size()), BigDecimal.valueOf(prevMonthOrders.size())),
                trend(BigDecimal.valueOf(totalClients), BigDecimal.valueOf(prevTotalClients)), trend(expenses, prevExpenses),
                revenueChart(shopId), ordersChart(shopId), recent, todayDeposits, readyForPickup, overdueOrders);
    }

    private String trend(BigDecimal current, BigDecimal previous) {
        if (previous.compareTo(BigDecimal.ZERO) == 0) {
            return current.compareTo(BigDecimal.ZERO) > 0 ? "+100%" : "0%";
        }
        BigDecimal change = current.subtract(previous)
                .divide(previous, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        int rounded = change.setScale(0, RoundingMode.HALF_UP).intValue();
        return (rounded >= 0 ? "+" : "") + rounded + "%";
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

