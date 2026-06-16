package com.creativpressing.api.repository;

import com.creativpressing.api.entity.CustomerOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.UUID;

public interface CustomerOrderRepository extends MongoRepository<CustomerOrder, UUID> {
    java.util.List<CustomerOrder> findByShopId(java.util.UUID shopId);

    java.util.List<CustomerOrder> findByShopIdAndStatus(java.util.UUID shopId,
            com.creativpressing.api.enums.OrderStatus status);

    java.util.List<CustomerOrder> findByShopIdAndPayment(java.util.UUID shopId,
            com.creativpressing.api.enums.PaymentStatus payment);

    java.util.List<CustomerOrder> findByShopIdAndReceivedAtBetween(java.util.UUID shopId, java.time.LocalDate start,
            java.time.LocalDate end);

    long countByShopId(java.util.UUID shopId);

    long countByClientId(java.util.UUID clientId);
}
