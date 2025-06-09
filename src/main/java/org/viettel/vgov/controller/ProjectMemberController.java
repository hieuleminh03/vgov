package org.viettel.vgov.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.viettel.vgov.dto.request.ProjectMemberRequestDto;
import org.viettel.vgov.dto.response.ProjectMemberResponseDto;
import org.viettel.vgov.dto.response.UserResponseDto;
import org.viettel.vgov.service.ProjectMemberService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    @GetMapping("/{id}/members")
    public ResponseEntity<List<ProjectMemberResponseDto>> getProjectMembers(@PathVariable Long id) {
        List<ProjectMemberResponseDto> members = projectMemberService.getProjectMembers(id);
        return ResponseEntity.ok(members);
    }

    @PostMapping("/{id}/members")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjectMemberResponseDto> addMemberToProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectMemberRequestDto requestDto) {
        ProjectMemberResponseDto member = projectMemberService.addMemberToProject(id, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(member);
    }

    @PutMapping("/{id}/members/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjectMemberResponseDto> updateMemberWorkload(
            @PathVariable Long id,
            @PathVariable Long userId,
            @Valid @RequestBody ProjectMemberRequestDto requestDto) {
        ProjectMemberResponseDto member = projectMemberService.updateMemberWorkload(id, userId, requestDto);
        return ResponseEntity.ok(member);
    }

    @DeleteMapping("/{id}/members/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> removeMemberFromProject(
            @PathVariable Long id,
            @PathVariable Long userId) {
        projectMemberService.removeMemberFromProject(id, userId);
        return ResponseEntity.ok(Map.of("message", "Member removed from project successfully"));
    }
}

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
class UserWorkloadController {

    private final ProjectMemberService projectMemberService;

    @GetMapping("/{id}/workload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> getUserWorkload(@PathVariable Long id) {
        UserResponseDto user = projectMemberService.getUserWorkload(id);
        return ResponseEntity.ok(user);
    }
}
