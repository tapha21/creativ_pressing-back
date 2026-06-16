package com.creativpressing.api.service;

import com.creativpressing.api.dto.request.EmployeeRequest;
import com.creativpressing.api.dto.response.EmployeeResponse;
import com.creativpressing.api.entity.Employee;
import com.creativpressing.api.exception.BusinessException;
import com.creativpressing.api.exception.ResourceNotFoundException;
import com.creativpressing.api.mapper.AppMapper;
import com.creativpressing.api.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository repo;
    private final ShopService shopService;

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

    public EmployeeResponse create(EmployeeRequest request) {
        checkPhoneIsAvailable(request.shopId(), request.phone());

        Employee employee = new Employee();
        shopService.getEntity(request.shopId());
        employee.setShopId(request.shopId());
        AppMapper.updateEmployee(employee, request);

        return AppMapper.toEmployeeResponse(repo.save(employee));
    }

    public EmployeeResponse update(UUID id, EmployeeRequest request) {
        Employee employee = getEntity(id);
        AppMapper.updateEmployee(employee, request);

        return AppMapper.toEmployeeResponse(repo.save(employee));
    }

    public void delete(UUID id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Employe introuvable");
        }

        repo.deleteById(id);
    }

    private void checkPhoneIsAvailable(UUID shopId, String phone) {
        if (repo.existsByShopIdAndPhone(shopId, phone)) {
            throw new BusinessException("Ce numero employe existe deja dans cette boutique");
        }
    }
}
