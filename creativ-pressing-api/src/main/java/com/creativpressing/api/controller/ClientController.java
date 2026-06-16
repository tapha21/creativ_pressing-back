package com.creativpressing.api.controller;

import com.creativpressing.api.dto.request.ClientRequest;
import com.creativpressing.api.dto.response.ClientResponse;
import com.creativpressing.api.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService service;

    @GetMapping
    public List<ClientResponse> byShop(@RequestParam UUID shopId, @RequestParam(required = false) String search) {
        return service.findByShop(shopId, search);
    }

    @PostMapping
    public ResponseEntity<ClientResponse> create(@Valid @RequestBody ClientRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r));
    }

    @PutMapping("/{id}")
    public ClientResponse update(@PathVariable UUID id, @Valid @RequestBody ClientRequest r) {
        return service.update(id, r);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
