package com.creativpressing.api.controller;

import com.creativpressing.api.dto.request.EmployeeRequest;
import com.creativpressing.api.dto.response.EmployeeResponse;
import com.creativpressing.api.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService service;

    @GetMapping
    public List<EmployeeResponse> byShop(@RequestParam UUID shopId, @RequestParam(required = false) Boolean active) {
        return service.findByShop(shopId, active);
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody EmployeeRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r));
    }

    @PutMapping("/{id}")
    public EmployeeResponse update(@PathVariable UUID id, @Valid @RequestBody EmployeeRequest r) {
        return service.update(id, r);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
