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
@Table(name = "pressing_shops")
public class PressingShop extends BaseEntity {
    @Column(nullable = false, length = 150)
    private String name;
    @Column(nullable = false, length = 150)
    private String ownerName;
    @Column(nullable = false, length = 30)
    private String phone;
    @Column(nullable = false, length = 100)
    private String city;
    @Column(nullable = false, length = 255)
    private String address;
    @Column(nullable = false, unique = true, length = 180)
    private String email;
    @Column(length = 255)
    private String passwordHash;
    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;
    @Builder.Default
    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Client> clients = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerOrder> orders = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expense> expenses = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Employee> employees = new ArrayList<>();
}
