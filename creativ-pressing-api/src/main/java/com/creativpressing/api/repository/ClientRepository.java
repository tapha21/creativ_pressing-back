package com.creativpressing.api.repository;

import com.creativpressing.api.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
    java.util.List<Client> findByShopId(java.util.UUID shopId);

    java.util.List<Client> findByShopIdAndNameContainingIgnoreCase(java.util.UUID shopId, String name);

    boolean existsByShopIdAndPhone(java.util.UUID shopId, String phone);

    long countByShopId(java.util.UUID shopId);
}
