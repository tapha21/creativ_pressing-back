package com.creativpressing.api.controller;

import com.creativpressing.api.dto.request.PhotoItemRequest;
import com.creativpressing.api.dto.response.PhotoItemResponse;
import com.creativpressing.api.enums.PhotoType;
import com.creativpressing.api.security.SecurityUtils;
import com.creativpressing.api.service.PhotoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/photos")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('OWNER','ADMIN')")
public class PhotoController {
    private final PhotoService service;

    @GetMapping
    public List<PhotoItemResponse> byOrder(@RequestParam(required = false) UUID orderId,
            @RequestParam(required = false) UUID shopId) {
        if (orderId != null) {
            return service.findByOrder(orderId);
        }
        return service.findByShop(SecurityUtils.resolveShopId(shopId));
    }

    @PostMapping
    public ResponseEntity<PhotoItemResponse> create(@Valid @RequestBody PhotoItemRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r));
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PhotoItemResponse> upload(@RequestParam UUID orderId, @RequestParam PhotoType type,
            @RequestParam(required = false) LocalDate date, @RequestParam MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.upload(orderId, type, date, file));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
