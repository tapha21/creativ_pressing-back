package com.creativpressing.api.mapper;

import com.creativpressing.api.dto.request.*;
import com.creativpressing.api.dto.response.*;
import com.creativpressing.api.entity.*;
import java.util.UUID;

public final class AppMapper {
    private AppMapper() {
    }

    public static ShopResponse toShopResponse(PressingShop s) {
        return new ShopResponse(s.getId(), s.getName(), s.getOwnerName(), s.getPhone(), s.getCity(), s.getAddress(),
                s.getActive(), s.getCreatedAt());
    }

    public static ClientResponse toClientResponse(Client c, long totalOrders) {
        return new ClientResponse(c.getId(), c.getName(), c.getPhone(), c.getAddress(), c.getCity(), c.getCreatedAt(),
                totalOrders);
    }

    public static OrderResponse toOrderResponse(CustomerOrder o) {
        return new OrderResponse(o.getId(), o.getClientId(), o.getClientName(), o.getClientPhone(), o.getItems(),
                o.getAmount(), o.getStatus(), o.getPayment(), o.getReceivedAt(), o.getDeliveryAt(),
                o.getAttachmentName(), o.getCreatedAt());
    }

    public static ExpenseResponse toExpenseResponse(Expense e) {
        return new ExpenseResponse(e.getId(), e.getCategory(), e.getDescription(), e.getAmount(), e.getDate());
    }

    public static EmployeeResponse toEmployeeResponse(Employee e) {
        return new EmployeeResponse(e.getId(), e.getName(), e.getRole(), e.getPhone(), e.getEmail(), e.getJoinedAt(),
                e.getActive());
    }

    public static PhotoItemResponse toPhotoItemResponse(PhotoItem p) {
        return new PhotoItemResponse(p.getId(), p.getOrderId(), p.getType(), p.getUrl(), p.getDate());
    }

    public static void updateShop(PressingShop s, ShopRequest r) {
        s.setName(r.name());
        s.setOwnerName(r.ownerName());
        s.setPhone(r.phone());
        s.setCity(r.city());
        s.setAddress(r.address());
        s.setEmail(r.email());
        if (r.password() != null && !r.password().isBlank()) {
            s.setPasswordHash("{noop}" + r.password());
        }
    }

    public static void updateClient(Client c, ClientRequest r) {
        c.setName(r.name());
        c.setPhone(r.phone());
        c.setAddress(r.address());
        c.setCity(r.city());
    }

    public static void updateOrder(CustomerOrder o, OrderRequest r) {
        o.setClientName(r.clientName());
        o.setClientPhone(r.clientPhone());
        o.setItems(r.items());
        o.setAmount(r.amount());
        o.setStatus(r.status());
        o.setPayment(r.payment());
        o.setReceivedAt(r.receivedAt());
        o.setDeliveryAt(r.deliveryAt());
        o.setAttachmentName(r.attachmentName());
    }

    public static void updateExpense(Expense e, ExpenseRequest r) {
        e.setCategory(r.category());
        e.setDescription(r.description());
        e.setAmount(r.amount());
        e.setDate(r.date());
    }

    public static void updateEmployee(Employee e, EmployeeRequest r) {
        e.setName(r.name());
        e.setRole(r.role());
        e.setPhone(r.phone());
        e.setEmail(r.email());
        e.setJoinedAt(r.joinedAt());
        e.setActive(r.active() == null ? true : r.active());
        if (r.password() != null && !r.password().isBlank())
            e.setPasswordHash("{noop}" + r.password());
    }
}
