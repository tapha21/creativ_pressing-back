package com.creativpressing.api.controller;

import com.creativpressing.api.dto.request.ShopRequest;
import com.creativpressing.api.dto.response.ShopResponse;
import com.creativpressing.api.service.ShopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/shops")
@RequiredArgsConstructor
public class ShopController {
    private final ShopService service;

    @GetMapping
    public List<ShopResponse> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ShopResponse one(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<ShopResponse> create(@Valid @RequestBody ShopRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r));
    }

    @PutMapping("/{id}")
    public ShopResponse update(@PathVariable UUID id, @Valid @RequestBody ShopRequest r) {
        return service.update(id, r);
    }
}
