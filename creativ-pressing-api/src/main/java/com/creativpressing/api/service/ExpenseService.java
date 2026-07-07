package com.creativpressing.api.service;

import com.creativpressing.api.dto.request.ExpenseRequest;
import com.creativpressing.api.dto.response.ExpenseResponse;
import com.creativpressing.api.entity.Expense;
import com.creativpressing.api.enums.ExpenseCategory;
import com.creativpressing.api.exception.ResourceNotFoundException;
import com.creativpressing.api.mapper.AppMapper;
import com.creativpressing.api.repository.ExpenseRepository;
import com.creativpressing.api.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository repo;
    private final ShopService shopService;

    public List<ExpenseResponse> findByShop(UUID shopId, ExpenseCategory category) {
        List<Expense> expenses = category == null
                ? repo.findByShopId(shopId)
                : repo.findByShopIdAndCategory(shopId, category);

        return expenses.stream()
                .map(AppMapper::toExpenseResponse)
                .toList();
    }

    public Expense getEntity(UUID id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Depense introuvable"));
    }

    public ExpenseResponse create(ExpenseRequest request, UUID shopId) {
        Expense expense = new Expense();
        shopService.getEntity(shopId);
        expense.setShopId(shopId);
        AppMapper.updateExpense(expense, request);

        return AppMapper.toExpenseResponse(repo.save(expense));
    }

    public ExpenseResponse update(UUID id, ExpenseRequest request) {
        Expense expense = getEntity(id);
        SecurityUtils.assertShopAccess(expense.getShopId());
        AppMapper.updateExpense(expense, request);

        return AppMapper.toExpenseResponse(repo.save(expense));
    }

    public void delete(UUID id) {
        Expense expense = getEntity(id);
        SecurityUtils.assertShopAccess(expense.getShopId());

        repo.deleteById(id);
    }
}
