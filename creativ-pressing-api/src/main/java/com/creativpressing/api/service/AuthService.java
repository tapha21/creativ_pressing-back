package com.creativpressing.api.service;

import com.creativpressing.api.dto.request.LoginRequest;
import com.creativpressing.api.dto.request.SignupRequest;
import com.creativpressing.api.dto.response.AuthResponse;
import com.creativpressing.api.entity.Employee;
import com.creativpressing.api.entity.PressingShop;
import com.creativpressing.api.enums.EmployeeRole;
import com.creativpressing.api.enums.SubscriptionPlan;
import com.creativpressing.api.enums.SubscriptionStatus;
import com.creativpressing.api.exception.BusinessException;
import com.creativpressing.api.mapper.AppMapper;
import com.creativpressing.api.repository.EmployeeRepository;
import com.creativpressing.api.repository.PressingShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
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
                .subscriptionPlan(SubscriptionPlan.PREMIUM)
                .subscriptionStatus(SubscriptionStatus.TRIAL)
                .trialEndsAt(LocalDate.now().plusDays(14))
                .active(true)
                .build();
        shopRepo.save(shop);

        Employee owner = Employee.builder()
                .shopId(shop.getId())
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
        PressingShop shop = shopRepo.findById(employee.getShopId())
                .orElseThrow(() -> new BusinessException("Boutique introuvable"));
        return new AuthResponse(shop.getId(), shop.getName(), employee.getId(), employee.getName(), employee.getEmail(),
                employee.getRole().getLabel(), AppMapper.planLabel(shop),
                AppMapper.statusLabel(shop), shop.getTrialEndsAt() == null ? null : shop.getTrialEndsAt().toString(),
                shop.getSubscriptionEndsAt() == null ? null : shop.getSubscriptionEndsAt().toString(), shop.getLogoUrl());
    }

    private boolean matches(String rawPassword, String passwordHash) {
        if (passwordHash == null || passwordHash.isBlank()) {
            return false;
        }
        return passwordHash.equals("{noop}" + rawPassword) || passwordHash.equals(rawPassword);
    }
}
