package com.creativpressing.api.entity;

import com.creativpressing.api.enums.SubscriptionPlan;
import com.creativpressing.api.enums.SubscriptionStatus;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("pressing_shops")
public class PressingShop extends BaseEntity {
    private String name;
    private String ownerName;
    private String phone;
    private String city;
    private String address;
    @Indexed(unique = true)
    private String email;
    private String passwordHash;
    private String logoUrl;
    @Builder.Default
    private SubscriptionPlan subscriptionPlan = SubscriptionPlan.BASIC;
    @Builder.Default
    private SubscriptionStatus subscriptionStatus = SubscriptionStatus.TRIAL;
    private LocalDate trialEndsAt;
    private LocalDate subscriptionEndsAt;
    @Builder.Default
    private Boolean active = true;
}
