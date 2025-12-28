package com.example.article_api.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PostCreateDTO(@NotNull String caption, @NotNull String userId, String mediaUrl, List<String> tags) {
}
