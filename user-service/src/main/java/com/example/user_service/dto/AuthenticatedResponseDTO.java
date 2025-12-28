package com.example.user_service.dto;

import lombok.Builder;

@Builder
public record AuthenticatedResponseDTO(String jwt, String userName, int expiry) {
}
