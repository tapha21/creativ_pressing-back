package com.creativpressing.api.repository;

import com.creativpressing.api.entity.PressingShop;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PressingShopRepository extends JpaRepository<PressingShop, UUID> {
    java.util.Optional<PressingShop> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailIgnoreCase(String email);
}
