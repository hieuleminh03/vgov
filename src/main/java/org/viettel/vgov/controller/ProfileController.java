package org.viettel.vgov.controller;

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
public class ProfileController {
    
    private final ProfileService profileService;
    
    @GetMapping
    public ResponseEntity<UserResponseDto> getCurrentProfile() {
        UserResponseDto profile = profileService.getCurrentProfile();
        return ResponseEntity.ok(profile);
    }
    
    @PutMapping("/photo")
    public ResponseEntity<UserResponseDto> updateProfilePhoto(@Valid @RequestBody ProfileUpdateRequestDto requestDto) {
        UserResponseDto profile = profileService.updateProfilePhoto(requestDto);
        return ResponseEntity.ok(profile);
    }
    
    @DeleteMapping("/photo")
    public ResponseEntity<Map<String, String>> removeProfilePhoto() {
        profileService.removeProfilePhoto();
        return ResponseEntity.ok(Map.of("message", "Profile photo removed successfully"));
    }
    
    @PutMapping("/password")
    public ResponseEntity<Map<String, String>> changePassword(@Valid @RequestBody PasswordChangeRequestDto requestDto) {
        profileService.changePassword(requestDto);
        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }
}
