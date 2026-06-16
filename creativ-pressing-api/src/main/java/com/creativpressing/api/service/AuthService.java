package com.creativpressing.api.service;

import com.creativpressing.api.dto.request.LoginRequest;
import com.creativpressing.api.dto.request.SignupRequest;
import com.creativpressing.api.dto.response.AuthResponse;
import com.creativpressing.api.entity.Employee;
import com.creativpressing.api.entity.PressingShop;
import com.creativpressing.api.enums.EmployeeRole;
import com.creativpressing.api.exception.BusinessException;
import com.creativpressing.api.repository.EmployeeRepository;
import com.creativpressing.api.repository.PressingShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final PressingShopRepository shopRepo;
    private final EmployeeRepository employeeRepo;

    public AuthResponse login(LoginRequest request) {
        Employee employee = employeeRepo.findByEmailIgnoreCaseAndActive(request.email(), true)
                .orElseThrow(() -> new BusinessException("Identifiants incorrects"));

        if (!matches(request.password(), employee.getPasswordHash())) {
            throw new BusinessException("Identifiants incorrects");
        }

        return toAuthResponse(employee);
    }

    public AuthResponse signup(SignupRequest request) {
        if (employeeRepo.existsByEmailIgnoreCase(request.email()) || shopRepo.existsByEmailIgnoreCase(request.email())) {
            throw new BusinessException("Cet email est deja utilise");
        }

        PressingShop shop = PressingShop.builder()
                .name(request.shopName())
                .ownerName(request.ownerName())
                .phone(request.phone())
                .city(request.city())
                .address(request.address())
                .email(request.email())
                .passwordHash("{noop}" + request.password())
                .active(true)
                .build();
        shopRepo.save(shop);

        Employee owner = Employee.builder()
                .shop(shop)
                .name(request.ownerName())
                .role(EmployeeRole.OWNER)
                .phone(request.phone())
                .email(request.email())
                .passwordHash("{noop}" + request.password())
                .joinedAt(LocalDate.now())
                .active(true)
                .build();

        return toAuthResponse(employeeRepo.save(owner));
    }

    private AuthResponse toAuthResponse(Employee employee) {
        PressingShop shop = employee.getShop();
        return new AuthResponse(shop.getId(), shop.getName(), employee.getId(), employee.getName(), employee.getEmail(),
                employee.getRole().getLabel());
    }

    private boolean matches(String rawPassword, String passwordHash) {
        if (passwordHash == null || passwordHash.isBlank()) {
            return false;
        }
        return passwordHash.equals("{noop}" + rawPassword) || passwordHash.equals(rawPassword);
    }
}
