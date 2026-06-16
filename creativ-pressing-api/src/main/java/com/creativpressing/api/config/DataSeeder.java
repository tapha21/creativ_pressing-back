package com.creativpressing.api.config;

import com.creativpressing.api.entity.Client;
import com.creativpressing.api.entity.CustomerOrder;
import com.creativpressing.api.entity.Employee;
import com.creativpressing.api.entity.Expense;
import com.creativpressing.api.entity.PhotoItem;
import com.creativpressing.api.entity.PressingShop;
import com.creativpressing.api.enums.EmployeeRole;
import com.creativpressing.api.enums.ExpenseCategory;
import com.creativpressing.api.enums.OrderStatus;
import com.creativpressing.api.enums.PaymentStatus;
import com.creativpressing.api.enums.PhotoType;
import com.creativpressing.api.repository.ClientRepository;
import com.creativpressing.api.repository.CustomerOrderRepository;
import com.creativpressing.api.repository.EmployeeRepository;
import com.creativpressing.api.repository.ExpenseRepository;
import com.creativpressing.api.repository.PhotoItemRepository;
import com.creativpressing.api.repository.PressingShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final PressingShopRepository shopRepo;
    private final ClientRepository clientRepo;
    private final CustomerOrderRepository orderRepo;
    private final ExpenseRepository expenseRepo;
    private final EmployeeRepository employeeRepo;
    private final PhotoItemRepository photoRepo;

    @Value("${app.seed.enabled:true}")
    private boolean enabled;

    @Override
    @Transactional
    public void run(String... args) {
        if (!enabled || shopRepo.count() > 0) {
            return;
        }

        seedShop("Pressing Tapha", "Tapha", "tapha@creativpressing.sn", "+221771112233", "Dakar",
                "Liberte 6, Dakar");
        seedShop("Pressing Lamine", "Lamine", "lamine@creativpressing.sn", "+221772223344", "Thies",
                "Grand Standing, Thies");

        log.info("Seed Creativ Pressing charge avec les comptes tapha@creativpressing.sn et lamine@creativpressing.sn");
    }

    private void seedShop(String shopName, String ownerName, String email, String phone, String city, String address) {
        PressingShop shop = PressingShop.builder()
                .name(shopName)
                .ownerName(ownerName)
                .phone(phone)
                .city(city)
                .address(address)
                .email(email)
                .passwordHash("{noop}1234")
                .active(true)
                .build();
        shopRepo.save(shop);

        Employee owner = Employee.builder()
                .shop(shop)
                .name(ownerName)
                .role(EmployeeRole.OWNER)
                .phone(phone)
                .email(email)
                .joinedAt(LocalDate.now().minusMonths(8))
                .active(true)
                .passwordHash("{noop}1234")
                .build();
        Employee employee = Employee.builder()
                .shop(shop)
                .name("Agent " + ownerName)
                .role(EmployeeRole.EMPLOYEE)
                .phone(phone.replace("77", "76"))
                .email("agent." + ownerName.toLowerCase() + "@creativpressing.sn")
                .joinedAt(LocalDate.now().minusMonths(3))
                .active(true)
                .passwordHash("{noop}1234")
                .build();
        employeeRepo.saveAll(List.of(owner, employee));

        Client c1 = Client.builder().shop(shop).name("Client " + ownerName + " 1").phone("+221700001111")
                .address("Quartier centre").city(city).build();
        Client c2 = Client.builder().shop(shop).name("Client " + ownerName + " 2").phone("+221700002222")
                .address("Avenue principale").city(city).build();
        clientRepo.saveAll(List.of(c1, c2));

        LocalDate today = LocalDate.now();
        List<CustomerOrder> orders = new ArrayList<>();
        orders.add(CustomerOrder.builder().shop(shop).client(c1).clientName(c1.getName()).clientPhone(c1.getPhone())
                .items("3 chemises, 1 pantalon").amount(BigDecimal.valueOf(4500)).status(OrderStatus.RECEIVED)
                .payment(PaymentStatus.UNPAID).receivedAt(today).deliveryAt(today.plusDays(2))
                .attachmentName("ticket-001.jpg").build());
        orders.add(CustomerOrder.builder().shop(shop).client(c2).clientName(c2.getName()).clientPhone(c2.getPhone())
                .items("2 boubous, 1 robe").amount(BigDecimal.valueOf(8500)).status(OrderStatus.WASHING)
                .payment(PaymentStatus.PARTIALLY_PAID).receivedAt(today.minusDays(1)).deliveryAt(today.plusDays(1))
                .build());
        orders.add(CustomerOrder.builder().shop(shop).client(c1).clientName(c1.getName()).clientPhone(c1.getPhone())
                .items("1 costume complet").amount(BigDecimal.valueOf(6000)).status(OrderStatus.READY)
                .payment(PaymentStatus.PAID).receivedAt(today.minusDays(3)).deliveryAt(today).build());
        orderRepo.saveAll(orders);

        expenseRepo.saveAll(List.of(
                Expense.builder().shop(shop).category(ExpenseCategory.WATER).description("Facture eau")
                        .amount(BigDecimal.valueOf(12000)).date(today.minusDays(4)).build(),
                Expense.builder().shop(shop).category(ExpenseCategory.ELECTRICITY).description("Recharge electricite")
                        .amount(BigDecimal.valueOf(25000)).date(today.minusDays(3)).build(),
                Expense.builder().shop(shop).category(ExpenseCategory.PRODUCTS).description("Produits de nettoyage")
                        .amount(BigDecimal.valueOf(18000)).date(today.minusDays(2)).build()));

        photoRepo.saveAll(List.of(
                PhotoItem.builder().order(orders.get(0)).type(PhotoType.BEFORE)
                        .url("https://images.unsplash.com/photo-1517677208171-0bc6725a3e60").date(today).build(),
                PhotoItem.builder().order(orders.get(2)).type(PhotoType.AFTER)
                        .url("https://images.unsplash.com/photo-1593030761757-71fae45fa0e7").date(today).build()));
    }
}
