package com.creativpressing.api.entity;

import com.creativpressing.api.enums.*;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_orders")
public class CustomerOrder extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", nullable = false)
    private PressingShop shop;
    @Column(nullable = false, length = 150)
    private String clientName;
    @Column(length = 30)
    private String clientPhone;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String items;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus status;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PaymentStatus payment;
    @Column(nullable = false)
    private LocalDate receivedAt;
    @Column(nullable = false)
    private LocalDate deliveryAt;
    @Column(length = 255)
    private String attachmentName;
    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhotoItem> photos = new ArrayList<>();
}
