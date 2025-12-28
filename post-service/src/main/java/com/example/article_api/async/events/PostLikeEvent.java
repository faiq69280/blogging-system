package com.example.article_api.async.events;

public record PostLikeEvent(PostUser likedBy, String authorId, String postId, int likesCount) {
    public static record PostUser(String userId, String userName) {}
}
