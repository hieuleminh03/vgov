package org.viettel.vgov.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.viettel.vgov.dto.response.DashboardResponseDto;
import org.viettel.vgov.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DashboardController {
    
    private final DashboardService dashboardService;
    
    @GetMapping("/overview")
    public ResponseEntity<DashboardResponseDto> getDashboardOverview() {
        DashboardResponseDto dashboard = dashboardService.getDashboardData();
        return ResponseEntity.ok(dashboard);
    }
}
