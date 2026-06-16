package com.creativpressing.api.controller;

import com.creativpressing.api.dto.request.ExpenseRequest;
import com.creativpressing.api.dto.response.ExpenseResponse;
import com.creativpressing.api.enums.ExpenseCategory;
import com.creativpressing.api.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService service;

    @GetMapping
    public List<ExpenseResponse> byShop(@RequestParam UUID shopId,
            @RequestParam(required = false) ExpenseCategory category) {
        return service.findByShop(shopId, category);
    }

    @PostMapping
    public ResponseEntity<ExpenseResponse> create(@Valid @RequestBody ExpenseRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r));
    }

    @PutMapping("/{id}")
    public ExpenseResponse update(@PathVariable UUID id, @Valid @RequestBody ExpenseRequest r) {
        return service.update(id, r);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
