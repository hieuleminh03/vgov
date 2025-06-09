package org.viettel.vgov.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.viettel.vgov.dto.request.LoginRequestDto;
import org.viettel.vgov.dto.response.JwtResponseDto;
import org.viettel.vgov.dto.response.UserResponseDto;
import org.viettel.vgov.service.AuthService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest) {
        JwtResponseDto response = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logoutUser() {
        authService.logout();
        return ResponseEntity.ok(Map.of("message", "User logged out successfully"));
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newToken = authService.refreshToken(email);
        return ResponseEntity.ok(Map.of("token", newToken, "type", "Bearer"));
    }
    
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser() {
        UserResponseDto user = authService.getCurrentUser();
        return ResponseEntity.ok(user);
    }
}
