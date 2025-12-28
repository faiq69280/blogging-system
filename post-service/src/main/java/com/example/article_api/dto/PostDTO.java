package com.example.article_api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record PostDTO(@NotNull  String postId, String caption, List<String> tags, String url) {

}
