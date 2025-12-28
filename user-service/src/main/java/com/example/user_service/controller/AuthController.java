package com.example.user_service.controller;

import com.example.user_service.dto.AuthenticatedResponseDTO;
import com.example.user_service.dto.UserDTO;
import com.example.user_service.dto.UserWithProfileDTO;
import com.example.user_service.exception.ConflictException;
import com.example.user_service.model.User;
import com.example.user_service.model.UserProfile;
import com.example.user_service.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserWithProfileDTO> register(@Valid @RequestBody UserDTO userDTO) throws ConflictException {
        User userToCreate = User.builder().name(userDTO.userName())
                .password(userDTO.password())
                .build();

        UserProfile userProfile = UserProfile.builder().email(userDTO.email())
                .avatarUrl(userDTO.avatarUrl())
                .email(userDTO.email())
                .user(userToCreate)
                .build();

        userToCreate.setUserProfile(userProfile);
        return ResponseEntity.ok(authService.create(userToCreate, userDTO.roles()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticatedResponseDTO> login(@NotNull @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(authService.verifyUserCredentials(userDTO));
    }
}
