package com.creativpressing.api.entity;

import com.creativpressing.api.enums.OrderStatus;
import com.creativpressing.api.enums.PaymentStatus;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("customer_orders")
public class CustomerOrder extends BaseEntity {
    private UUID clientId;
    private UUID shopId;
    private String clientName;
    private String clientPhone;
    private String items;
    private BigDecimal amount;
    private OrderStatus status;
    private PaymentStatus payment;
    private LocalDate receivedAt;
    private LocalDate deliveryAt;
    private String attachmentName;
}
