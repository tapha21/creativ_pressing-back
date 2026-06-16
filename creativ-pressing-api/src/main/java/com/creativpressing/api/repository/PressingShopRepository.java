package com.creativpressing.api.repository;

import com.creativpressing.api.entity.PressingShop;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.UUID;

public interface PressingShopRepository extends MongoRepository<PressingShop, UUID> {
    java.util.Optional<PressingShop> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailIgnoreCase(String email);
}
