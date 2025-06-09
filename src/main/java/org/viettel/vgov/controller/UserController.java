package org.viettel.vgov.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.viettel.vgov.dto.request.UserRequestDto;
import org.viettel.vgov.dto.response.PagedResponse;
import org.viettel.vgov.dto.response.StandardResponse;
import org.viettel.vgov.dto.response.UserResponseDto;
import org.viettel.vgov.model.User;
import org.viettel.vgov.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse<PagedResponse<UserResponseDto>>> getAllUsers(
            @PageableDefault(size = 20) Pageable pageable) {
        PagedResponse<UserResponseDto> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(StandardResponse.success(users));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse<UserResponseDto>> getUserById(@PathVariable Long id) {
        UserResponseDto user = userService.getUserById(id);
        return ResponseEntity.ok(StandardResponse.success(user));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse<UserResponseDto>> createUser(@Valid @RequestBody UserRequestDto requestDto) {
        UserResponseDto user = userService.createUser(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(StandardResponse.success(user, "User created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse<UserResponseDto>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDto requestDto) {
        UserResponseDto user = userService.updateUser(id, requestDto);
        return ResponseEntity.ok(StandardResponse.success(user, "User updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse<String>> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.ok(StandardResponse.success("User deactivated successfully"));
    }
    
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse<UserResponseDto>> changeUserRole(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        User.Role newRole = User.Role.valueOf(request.get("role"));
        UserResponseDto user = userService.changeUserRole(id, newRole);
        return ResponseEntity.ok(StandardResponse.success(user, "User role updated successfully"));
    }
    
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse<UserResponseDto>> activateDeactivateUser(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> request) {
        boolean isActive = request.get("isActive");
        UserResponseDto user = userService.activateDeactivateUser(id, isActive);
        String message = isActive ? "User activated successfully" : "User deactivated successfully";
        return ResponseEntity.ok(StandardResponse.success(user, message));
    }
    
    @GetMapping("/{id}/workload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse<Map<String, Object>>> getUserWorkload(@PathVariable Long id) {
        Map<String, Object> workload = userService.getUserWorkload(id);
        return ResponseEntity.ok(StandardResponse.success(workload));
    }
}
