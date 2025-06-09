package org.viettel.vgov.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.viettel.vgov.model.Project;
import org.viettel.vgov.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/system")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SystemController {

    @GetMapping("/health")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "timestamp", String.valueOf(System.currentTimeMillis()),
                "application", "V-GOV Backend"
        ));
    }

    @GetMapping("/version")
    public ResponseEntity<Map<String, String>> getVersion() {
        return ResponseEntity.ok(Map.of(
                "version", "1.0.0",
                "build", "2024-01-01",
                "environment", "development"
        ));
    }
}

@RestController
@RequestMapping("/api/lookup")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
class LookupController {

    @GetMapping("/roles")
    public ResponseEntity<List<User.Role>> getRoles() {
        return ResponseEntity.ok(Arrays.asList(User.Role.values()));
    }

    @GetMapping("/project-types")
    public ResponseEntity<List<Project.ProjectType>> getProjectTypes() {
        return ResponseEntity.ok(Arrays.asList(Project.ProjectType.values()));
    }

    @GetMapping("/project-statuses")
    public ResponseEntity<List<Project.Status>> getProjectStatuses() {
        return ResponseEntity.ok(Arrays.asList(Project.Status.values()));
    }
}
