package com.creativpressing.api.controller;

import com.creativpressing.api.dto.request.ShopActiveRequest;
import com.creativpressing.api.dto.request.ShopRequest;
import com.creativpressing.api.dto.request.ShopSubscriptionRequest;
import com.creativpressing.api.dto.response.ShopResponse;
import com.creativpressing.api.security.SecurityUtils;
import com.creativpressing.api.service.ShopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/shops")
@RequiredArgsConstructor
public class ShopController {
    private final ShopService service;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<ShopResponse> all(@RequestParam(required = false) String status) {
        return service.findAll(status);
    }

    @GetMapping("/{id}")
    public ShopResponse one(@PathVariable UUID id) {
        if (!SecurityUtils.isAdmin()) {
            SecurityUtils.assertShopAccess(id);
        }
        return service.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShopResponse> create(@Valid @RequestBody ShopRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    public ShopResponse update(@PathVariable UUID id, @Valid @RequestBody ShopRequest r) {
        if (!SecurityUtils.isAdmin()) {
            SecurityUtils.assertShopAccess(id);
        }
        return service.update(id, r);
    }

    @PatchMapping("/{id}/subscription")
    @PreAuthorize("hasRole('ADMIN')")
    public ShopResponse updateSubscription(@PathVariable UUID id, @RequestBody ShopSubscriptionRequest r) {
        return service.updateSubscription(id, r);
    }

    @PatchMapping("/{id}/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ShopResponse setActive(@PathVariable UUID id, @RequestBody ShopActiveRequest r) {
        return service.setActive(id, r);
    }
}
