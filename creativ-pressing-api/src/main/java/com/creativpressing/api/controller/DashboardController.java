package com.creativpressing.api.controller;

import com.creativpressing.api.dto.response.*;
import com.creativpressing.api.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService service;

    @GetMapping("/dashboard")
    public DashboardSummaryResponse summary(@RequestParam UUID shopId) {
        return service.summary(shopId);
    }

    @GetMapping("/reports")
    public ReportsDataResponse reports(@RequestParam UUID shopId) {
        return service.reports(shopId);
    }
}
