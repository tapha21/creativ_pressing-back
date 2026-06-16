package com.creativpressing.api.controller;

import com.creativpressing.api.dto.request.OrderRequest;
import com.creativpressing.api.dto.response.OrderResponse;
import com.creativpressing.api.enums.*;
import com.creativpressing.api.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;

    @GetMapping
    public List<OrderResponse> byShop(@RequestParam UUID shopId, @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) PaymentStatus payment) {
        return service.findByShop(shopId, status, payment);
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r));
    }

    @PutMapping("/{id}")
    public OrderResponse update(@PathVariable UUID id, @Valid @RequestBody OrderRequest r) {
        return service.update(id, r);
    }

    @PatchMapping("/{id}/status")
    public OrderResponse status(@PathVariable UUID id, @RequestParam OrderStatus status) {
        return service.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
