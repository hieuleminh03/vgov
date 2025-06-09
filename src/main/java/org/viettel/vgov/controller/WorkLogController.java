package org.viettel.vgov.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.viettel.vgov.dto.request.WorkLogRequestDto;
import org.viettel.vgov.dto.response.WorkLogResponseDto;
import org.viettel.vgov.service.WorkLogService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/work-logs")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class WorkLogController {
    
    private final WorkLogService workLogService;
    
    @GetMapping
    public ResponseEntity<List<WorkLogResponseDto>> getAllWorkLogs() {
        List<WorkLogResponseDto> workLogs = workLogService.getAllWorkLogs();
        return ResponseEntity.ok(workLogs);
    }
    
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PM') or @workLogService.canAccessUserWorkLogs(#userId, authentication.name)")
    public ResponseEntity<List<WorkLogResponseDto>> getWorkLogsByUserId(@PathVariable Long userId) {
        List<WorkLogResponseDto> workLogs = workLogService.getWorkLogsByUserId(userId);
        return ResponseEntity.ok(workLogs);
    }
    
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<WorkLogResponseDto>> getWorkLogsByProjectId(@PathVariable Long projectId) {
        List<WorkLogResponseDto> workLogs = workLogService.getWorkLogsByProjectId(projectId);
        return ResponseEntity.ok(workLogs);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('PM') or hasRole('DEV') or hasRole('BA') or hasRole('TEST')")
    public ResponseEntity<WorkLogResponseDto> createWorkLog(@Valid @RequestBody WorkLogRequestDto requestDto) {
        WorkLogResponseDto workLog = workLogService.createWorkLog(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(workLog);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PM') or hasRole('DEV') or hasRole('BA') or hasRole('TEST')")
    public ResponseEntity<WorkLogResponseDto> updateWorkLog(
            @PathVariable Long id,
            @Valid @RequestBody WorkLogRequestDto requestDto) {
        WorkLogResponseDto workLog = workLogService.updateWorkLog(id, requestDto);
        return ResponseEntity.ok(workLog);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PM') or hasRole('DEV') or hasRole('BA') or hasRole('TEST')")
    public ResponseEntity<Map<String, String>> deleteWorkLog(@PathVariable Long id) {
        workLogService.deleteWorkLog(id);
        return ResponseEntity.ok(Map.of("message", "Work log deleted successfully"));
    }
}
