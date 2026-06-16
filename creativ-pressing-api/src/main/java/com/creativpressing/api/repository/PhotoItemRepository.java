package com.creativpressing.api.repository;

import com.creativpressing.api.entity.PhotoItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.UUID;

public interface PhotoItemRepository extends MongoRepository<PhotoItem, UUID> {
    java.util.List<PhotoItem> findByOrderId(java.util.UUID orderId);

    java.util.List<PhotoItem> findByShopId(java.util.UUID shopId);
}
