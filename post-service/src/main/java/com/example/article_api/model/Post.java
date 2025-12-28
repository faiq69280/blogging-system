package com.example.article_api.model;

import com.example.article_api.exception.UnsupportedMediaException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Builder
@AllArgsConstructor
public class Post extends BaseEntity {
    @Column
    private String caption;

    @Column
    private String mediaUrl;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    @Column
    @Enumerated(EnumType.STRING)
    private PostMediaType mediaType;


    @Column(name = "user_id")
   private String userId;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "post")
    private List<PostLikes> postLikes;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "post")
    private List<Comment> comments;

    public Post() {}
    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }

    public String getMediaUrl() { return mediaUrl; }
    public void setMediaUrl(String mediaUrl) { this.mediaUrl = mediaUrl; }

    public PostMediaType getMediaType() { return mediaType; }
    public void setMediaType(PostMediaType mediaType) { this.mediaType = mediaType; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public List<Tag> getTags() { return tags; }
    public void setTags(List<Tag> tags) { this.tags = tags; }

    public List<PostLikes> getPostLikes() { return postLikes; }
    public void setPostLikes(List<PostLikes> postLikes) { this.postLikes = postLikes; }

    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }

    public void clearTags() {
        for (Tag tag : tags) {
            tag.getPosts().remove(this);
        }
        setTags(new ArrayList<>());
    }

    /**
     * Media Type used across the system to keep track of what kind of media does the post refer to.
     */
   public enum PostMediaType {
        VIDEO,
        IMAGE;

        public static PostMediaType fromContentType(String contentType) throws UnsupportedMediaException {
            if (contentType != null) {
                contentType = contentType.toLowerCase();
                if (contentType.startsWith("image/")) {
                    return IMAGE;
                } else if (contentType.startsWith("video/")) {
                    return VIDEO;
                }
            }
            throw new UnsupportedMediaException("%s not supported".formatted(contentType));
        }
    }
}
