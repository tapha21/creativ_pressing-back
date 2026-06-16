package com.creativpressing.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients", uniqueConstraints = @UniqueConstraint(name = "uk_client_shop_phone", columnNames = { "shop_id",
        "phone" }))
public class Client extends BaseEntity {
    @Column(nullable = false, length = 150)
    private String name;
    @Column(nullable = false, length = 30)
    private String phone;
    @Column(length = 255)
    private String address;
    @Column(length = 100)
    private String city;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", nullable = false)
    private PressingShop shop;
    @Builder.Default
    @OneToMany(mappedBy = "client")
    private List<CustomerOrder> orders = new ArrayList<>();
}
