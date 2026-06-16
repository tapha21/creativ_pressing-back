package com.creativpressing.api.entity;

import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("clients")
@CompoundIndex(name = "uk_client_shop_phone", def = "{'shopId': 1, 'phone': 1}", unique = true)
public class Client extends BaseEntity {
    private String name;
    private String phone;
    private String address;
    private String city;
    private UUID shopId;
}
