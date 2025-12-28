package com.example.article_api.domain;

import lombok.Builder;

@Builder
public class PostMetadata {
    private int likesCount = 0;
    private int viewsCount = 0;
    private int shareCount = 0;
    private int commentCount = 0;

    public PostMetadata(int likesCount, int viewsCount, int shareCount, int commentCount) {
        this.likesCount = likesCount;
        this.viewsCount = viewsCount;
        this.shareCount = shareCount;
        this.commentCount = commentCount;
    }

    public int getLikesCount() { return likesCount; }
    public void setLikesCount(int likesCount) { this.likesCount = likesCount; }

    public int getViewsCount() { return viewsCount; }
    public void setViewsCount(int viewsCount) { this.viewsCount = viewsCount; }

    public int getShareCount() { return shareCount; }
    public void setShareCount(int shareCount) { this.shareCount = shareCount; }

    public int getCommentCount() { return commentCount; }
    public void setCommentCount(int commentCount) { this.commentCount = commentCount; }

    public enum MetaDataKey {
        LIKES,
        VIEWS,
        SHARES,
        COMMENTS
    }
}
