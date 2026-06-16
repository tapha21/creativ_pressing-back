package com.creativpressing.api.repository;

import com.creativpressing.api.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
    java.util.List<Expense> findByShopId(java.util.UUID shopId);

    java.util.List<Expense> findByShopIdAndCategory(java.util.UUID shopId,
            com.creativpressing.api.enums.ExpenseCategory category);

    java.util.List<Expense> findByShopIdAndDateBetween(java.util.UUID shopId, java.time.LocalDate start,
            java.time.LocalDate end);
}
