package com.creativpressing.api.entity;

import com.creativpressing.api.enums.EmployeeRole;
import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("employees")
@CompoundIndex(name = "uk_employee_shop_phone", def = "{'shopId': 1, 'phone': 1}", unique = true)
public class Employee extends BaseEntity {
    private String name;
    private EmployeeRole role;
    private String phone;
    @Indexed
    private String email;
    private String passwordHash;
    private LocalDate joinedAt;
    @Builder.Default
    private Boolean active = true;
    private UUID shopId;
}
