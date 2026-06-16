package com.creativpressing.api.entity;

import com.creativpressing.api.enums.EmployeeRole;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employees", uniqueConstraints = @UniqueConstraint(name = "uk_employee_shop_phone", columnNames = {
        "shop_id", "phone" }))
public class Employee extends BaseEntity {
    @Column(nullable = false, length = 150)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private EmployeeRole role;
    @Column(nullable = false, length = 30)
    private String phone;
    @Column(length = 180)
    private String email;
    @Column(length = 255)
    private String passwordHash;
    @Column(nullable = false)
    private LocalDate joinedAt;
    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", nullable = false)
    private PressingShop shop;
}
