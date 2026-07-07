package com.creativpressing.api.controller;

import com.creativpressing.api.dto.response.*;
import com.creativpressing.api.security.SecurityUtils;
import com.creativpressing.api.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('OWNER','ADMIN')")
public class DashboardController {
    private final DashboardService service;

    @GetMapping("/dashboard")
    public DashboardSummaryResponse summary(@RequestParam(required = false) UUID shopId) {
        return service.summary(SecurityUtils.resolveShopId(shopId));
    }

    @GetMapping("/reports")
    public ReportsDataResponse reports(@RequestParam(required = false) UUID shopId) {
        return service.reports(SecurityUtils.resolveShopId(shopId));
    }
}
