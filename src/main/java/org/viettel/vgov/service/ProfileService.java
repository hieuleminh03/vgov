package org.viettel.vgov.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.viettel.vgov.dto.request.PasswordChangeRequestDto;
import org.viettel.vgov.dto.request.ProfileUpdateRequestDto;
import org.viettel.vgov.dto.response.UserResponseDto;
import org.viettel.vgov.exception.ResourceNotFoundException;
import org.viettel.vgov.mapper.UserMapper;
import org.viettel.vgov.model.User;
import org.viettel.vgov.repository.UserRepository;
import org.viettel.vgov.security.UserPrincipal;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    
    public UserResponseDto getCurrentProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        User currentUser = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        return userMapper.toResponseDto(currentUser);
    }
    
    public UserResponseDto updateProfilePhoto(ProfileUpdateRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        User currentUser = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        currentUser.setProfilePhotoUrl(requestDto.getProfilePhotoUrl());
        currentUser.setUpdatedBy(currentUser);
        
        User savedUser = userRepository.save(currentUser);
        return userMapper.toResponseDto(savedUser);
    }
    
    public void removeProfilePhoto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        User currentUser = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        currentUser.setProfilePhotoUrl(null);
        currentUser.setUpdatedBy(currentUser);
        
        userRepository.save(currentUser);
    }
    
    public void changePassword(PasswordChangeRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        User currentUser = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Validate current password
        if (!passwordEncoder.matches(requestDto.getCurrentPassword(), currentUser.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        
        // Validate new password confirmation
        if (!requestDto.getNewPassword().equals(requestDto.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }
        
        // Update password
        currentUser.setPasswordHash(passwordEncoder.encode(requestDto.getNewPassword()));
        currentUser.setUpdatedBy(currentUser);
        
        userRepository.save(currentUser);
    }
}
