package org.viettel.vgov.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.viettel.vgov.dto.response.AnalyticsResponseDto;
import org.viettel.vgov.service.AnalyticsService;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AnalyticsController {
    
    private final AnalyticsService analyticsService;
    
    @GetMapping("/projects")
    @PreAuthorize("hasRole('admin') or hasRole('pm')")
    public ResponseEntity<AnalyticsResponseDto> getProjectAnalytics() {
        AnalyticsResponseDto analytics = analyticsService.getProjectAnalytics();
        return ResponseEntity.ok(analytics);
    }
    
    @GetMapping("/employees")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<AnalyticsResponseDto> getEmployeeAnalytics() {
        AnalyticsResponseDto analytics = analyticsService.getEmployeeAnalytics();
        return ResponseEntity.ok(analytics);
    }
    
    @GetMapping("/workload")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<AnalyticsResponseDto> getWorkloadAnalytics() {
        AnalyticsResponseDto analytics = analyticsService.getWorkloadAnalytics();
        return ResponseEntity.ok(analytics);
    }
    
    @GetMapping("/project/{id}/timeline")
    @PreAuthorize("hasRole('admin') or hasRole('pm') or @projectSecurityService.canAccessProject(#id, authentication.name)")
    public ResponseEntity<AnalyticsResponseDto> getProjectTimeline(@PathVariable Long id) {
        AnalyticsResponseDto timeline = analyticsService.getProjectTimeline(id);
        return ResponseEntity.ok(timeline);
    }
}
