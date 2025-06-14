package org.viettel.vgov.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.viettel.vgov.dto.request.PasswordChangeRequestDto;
import org.viettel.vgov.dto.request.ProfileUpdateRequestDto;
import org.viettel.vgov.dto.response.UserResponseDto;
import org.viettel.vgov.service.ProfileService;

import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Profile Management", description = "User profile and personal settings")
@SecurityRequirement(name = "bearerAuth")
public class ProfileController {
    
    private final ProfileService profileService;
    
    @Operation(summary = "Get current user profile", description = "Retrieve current user's profile information")
    @GetMapping
    public ResponseEntity<UserResponseDto> getCurrentProfile() {
        UserResponseDto profile = profileService.getCurrentProfile();
        return ResponseEntity.ok(profile);
    }
    
    @Operation(summary = "Update profile photo", description = "Upload or update user's profile photo")
    @PutMapping("/photo")
    public ResponseEntity<UserResponseDto> updateProfilePhoto(@Valid @RequestBody ProfileUpdateRequestDto requestDto) {
        UserResponseDto profile = profileService.updateProfilePhoto(requestDto);
        return ResponseEntity.ok(profile);
    }
    
    @Operation(summary = "Remove profile photo", description = "Remove user's profile photo")
    @DeleteMapping("/photo")
    public ResponseEntity<Map<String, String>> removeProfilePhoto() {
        profileService.removeProfilePhoto();
        return ResponseEntity.ok(Map.of("message", "Profile photo removed successfully"));
    }
    
    @Operation(summary = "Change password", description = "Change user's password")
    @PutMapping("/password")
    public ResponseEntity<Map<String, String>> changePassword(@Valid @RequestBody PasswordChangeRequestDto requestDto) {
        profileService.changePassword(requestDto);
        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }
}
