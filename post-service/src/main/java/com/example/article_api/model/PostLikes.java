package com.example.article_api.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table
public class PostLikes {
    @EmbeddedId
    private PostLikeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("postId")
    @JoinColumn(name = "post_id")
    private Post post;

    public PostLikes() {}
    public String getUserId() { return id.getUserId(); }
    public String getPostId() { return id.getPostId(); }

    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }

    public void setId(PostLikeId postLikeId) { this.id = postLikeId; }
    public PostLikeId getId() { return id; }

    @Embeddable
    public static class PostLikeId implements Serializable {
        @Column(name = "user_id")
        private String userId;

        @Column(name = "post_id")
        private String postId;

        public PostLikeId() {}
        public PostLikeId(String userId, String postId) {
            this.userId = userId;
            this.postId = postId;
        }

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }

        public String getPostId() { return postId; }
        public void setPostId(String postId) { this.postId = postId; }

        @Override
        public boolean equals(Object o) {
            if (this == o) { return true; }
            if (!(o instanceof PostLikeId)) { return false; }

            PostLikeId that = (PostLikeId) o;
            return Objects.equals(userId, that.userId) &&
                    Objects.equals(postId, that.postId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, postId);
        }
    }
}
