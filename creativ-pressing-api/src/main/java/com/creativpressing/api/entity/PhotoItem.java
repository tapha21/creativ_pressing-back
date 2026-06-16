package com.creativpressing.api.entity;

import com.creativpressing.api.enums.PhotoType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "photo_items")
public class PhotoItem extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private CustomerOrder order;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PhotoType type;
    @Column(nullable = false, length = 500)
    private String url;
    @Column(nullable = false)
    private LocalDate date;
}
