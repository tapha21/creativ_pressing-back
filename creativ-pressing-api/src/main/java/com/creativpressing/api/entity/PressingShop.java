package com.creativpressing.api.entity;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

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
    @Builder.Default
    private Boolean active = true;
}
