package com.creativpressing.api.service;

import com.creativpressing.api.dto.request.EmployeeRequest;
import com.creativpressing.api.dto.response.EmployeeResponse;
import com.creativpressing.api.entity.Employee;
import com.creativpressing.api.enums.EmployeeRole;
import com.creativpressing.api.exception.BusinessException;
import com.creativpressing.api.exception.ResourceNotFoundException;
import com.creativpressing.api.mapper.AppMapper;
import com.creativpressing.api.repository.EmployeeRepository;
import com.creativpressing.api.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository repo;
    private final ShopService shopService;
    private final PasswordEncoder passwordEncoder;

    public List<EmployeeResponse> findByShop(UUID shopId, Boolean active) {
        List<Employee> employees = active == null
                ? repo.findByShopId(shopId)
                : repo.findByShopIdAndActive(shopId, active);

        return employees.stream()
                .map(AppMapper::toEmployeeResponse)
                .toList();
    }

    public Employee getEntity(UUID id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employe introuvable"));
    }

    public EmployeeResponse create(EmployeeRequest request, UUID shopId) {
        rejectAdminRole(request);
        checkPhoneIsAvailable(shopId, request.phone());
        checkEmailIsAvailable(request.email(), null);

        Employee employee = new Employee();
        shopService.getEntity(shopId);
        employee.setShopId(shopId);
        AppMapper.updateEmployee(employee, request);
        if (request.password() != null && !request.password().isBlank()) {
            employee.setPasswordHash(passwordEncoder.encode(request.password()));
        }

        return AppMapper.toEmployeeResponse(repo.save(employee));
    }

    public EmployeeResponse update(UUID id, EmployeeRequest request) {
        rejectAdminRole(request);
        Employee employee = getEntity(id);
        SecurityUtils.assertShopAccess(employee.getShopId());
        if (!employee.getPhone().equals(request.phone())) {
            checkPhoneIsAvailable(employee.getShopId(), request.phone());
        }
        checkEmailIsAvailable(request.email(), id);
        AppMapper.updateEmployee(employee, request);
        if (request.password() != null && !request.password().isBlank()) {
            employee.setPasswordHash(passwordEncoder.encode(request.password()));
        }

        return AppMapper.toEmployeeResponse(repo.save(employee));
    }

    public void delete(UUID id) {
        Employee employee = getEntity(id);
        SecurityUtils.assertShopAccess(employee.getShopId());

        repo.deleteById(id);
    }

    private void rejectAdminRole(EmployeeRequest request) {
        if (request.role() == EmployeeRole.ADMIN) {
            throw new BusinessException("Le rôle Administrateur ne peut pas être attribué depuis cet écran");
        }
    }

    private void checkPhoneIsAvailable(UUID shopId, String phone) {
        if (repo.existsByShopIdAndPhone(shopId, phone)) {
            throw new BusinessException("Ce numero employe existe deja dans cette boutique");
        }
    }

    private void checkEmailIsAvailable(String email, UUID currentEmployeeId) {
        if (email == null || email.isBlank()) {
            return;
        }

        repo.findByEmailIgnoreCase(email).ifPresent(existing -> {
            if (currentEmployeeId == null || !existing.getId().equals(currentEmployeeId)) {
                throw new BusinessException("Cet email employe existe deja");
            }
        });
    }
}
