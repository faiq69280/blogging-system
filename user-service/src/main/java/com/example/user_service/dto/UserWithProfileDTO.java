package com.example.user_service.dto;

import lombok.Builder;

import java.util.Map;

@Builder
public record UserWithProfileDTO(String userId, String userName, String email, String avatarUrl,
                                 Map<String, String> contactSubscription) {
}
