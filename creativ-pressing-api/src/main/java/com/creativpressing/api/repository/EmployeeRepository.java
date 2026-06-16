package com.creativpressing.api.repository;

import com.creativpressing.api.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    java.util.List<Employee> findByShopId(java.util.UUID shopId);

    java.util.List<Employee> findByShopIdAndActive(java.util.UUID shopId, Boolean active);

    boolean existsByShopIdAndPhone(java.util.UUID shopId, String phone);

    Optional<Employee> findByEmailIgnoreCaseAndActive(String email, Boolean active);

    boolean existsByEmailIgnoreCase(String email);
}
