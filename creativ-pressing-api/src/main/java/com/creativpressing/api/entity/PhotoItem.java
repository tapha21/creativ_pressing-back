package com.creativpressing.api.entity;

import com.creativpressing.api.enums.PhotoType;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("photo_items")
public class PhotoItem extends BaseEntity {
    private UUID orderId;
    private UUID shopId;
    private PhotoType type;
    private String url;
    private LocalDate date;
}
