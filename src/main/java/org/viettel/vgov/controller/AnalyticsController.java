package org.viettel.vgov.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Dashboard & Analytics", description = "Analytics data and reporting endpoints")
@SecurityRequirement(name = "bearerAuth")
public class AnalyticsController {
    
    private final AnalyticsService analyticsService;
    
    @Operation(summary = "Get project analytics", description = "Retrieve project analytics data (Admin/PM only)")
    @GetMapping("/projects")
    @PreAuthorize("hasRole('admin') or hasRole('pm')")
    public ResponseEntity<AnalyticsResponseDto> getProjectAnalytics() {
        AnalyticsResponseDto analytics = analyticsService.getProjectAnalytics();
        return ResponseEntity.ok(analytics);
    }
    
    @Operation(summary = "Get employee analytics", description = "Retrieve employee performance analytics (Admin only)")
    @GetMapping("/employees")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<AnalyticsResponseDto> getEmployeeAnalytics() {
        AnalyticsResponseDto analytics = analyticsService.getEmployeeAnalytics();
        return ResponseEntity.ok(analytics);
    }
    
    @Operation(summary = "Get workload analytics", description = "Retrieve team workload analytics (Admin only)")
    @GetMapping("/workload")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<AnalyticsResponseDto> getWorkloadAnalytics() {
        AnalyticsResponseDto analytics = analyticsService.getWorkloadAnalytics();
        return ResponseEntity.ok(analytics);
    }
    
    @Operation(summary = "Get project timeline", description = "Retrieve project timeline and milestones")
    @GetMapping("/project/{id}/timeline")
    @PreAuthorize("hasRole('admin') or hasRole('pm') or @projectSecurityService.canAccessProject(#id, authentication.name)")
    public ResponseEntity<AnalyticsResponseDto> getProjectTimeline(@PathVariable Long id) {
        AnalyticsResponseDto timeline = analyticsService.getProjectTimeline(id);
        return ResponseEntity.ok(timeline);
    }
}
