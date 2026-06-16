package com.creativpressing.api.repository;

import com.creativpressing.api.entity.PhotoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PhotoItemRepository extends JpaRepository<PhotoItem, UUID> {
    java.util.List<PhotoItem> findByOrderId(java.util.UUID orderId);

    java.util.List<PhotoItem> findByOrderShopId(java.util.UUID shopId);
}
