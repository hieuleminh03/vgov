package org.viettel.vgov.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.viettel.vgov.dto.request.ProjectRequestDto;
import org.viettel.vgov.dto.response.PagedResponse;
import org.viettel.vgov.dto.response.ProjectResponseDto;
import org.viettel.vgov.dto.response.StandardResponse;
import org.viettel.vgov.model.Project;
import org.viettel.vgov.service.ProjectService;

import java.util.Map;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProjectController {
    
    private final ProjectService projectService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PM') or hasRole('DEV') or hasRole('BA') or hasRole('TEST')")
    public ResponseEntity<StandardResponse<PagedResponse<ProjectResponseDto>>> getAllProjects(
            @PageableDefault(size = 20) Pageable pageable) {
        PagedResponse<ProjectResponseDto> projects = projectService.getAllProjects(pageable);
        return ResponseEntity.ok(StandardResponse.success(projects));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PM') or @projectSecurityService.canAccessProject(#id, authentication.name)")
    public ResponseEntity<StandardResponse<ProjectResponseDto>> getProjectById(@PathVariable Long id) {
        ProjectResponseDto project = projectService.getProjectById(id);
        return ResponseEntity.ok(StandardResponse.success(project));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse<ProjectResponseDto>> createProject(@Valid @RequestBody ProjectRequestDto requestDto) {
        ProjectResponseDto project = projectService.createProject(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(StandardResponse.success(project, "Project created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse<ProjectResponseDto>> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequestDto requestDto) {
        ProjectResponseDto project = projectService.updateProject(id, requestDto);
        return ResponseEntity.ok(StandardResponse.success(project, "Project updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse<String>> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(StandardResponse.success("Project deleted successfully"));
    }
    
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse<ProjectResponseDto>> updateProjectStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        Project.Status status = Project.Status.valueOf(request.get("status"));
        ProjectResponseDto project = projectService.updateProjectStatus(id, status);
        return ResponseEntity.ok(StandardResponse.success(project, "Project status updated successfully"));
    }
}
