package com.example.article_api.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class Comment extends BaseEntity {
    @Column
    private String textBody;

    @Column
    private String mediaUrl;

    @Column
    private Integer sequence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
   Comment parentComment;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentComment")
    Set<Comment> childComments = new HashSet<>();

    @Column(name = "user_id")
    String userId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    Post post;

    public Comment() {}

    public String getTextBody() { return textBody; }
    public void setTextBody(String textBody) { this.textBody = textBody; }

    public String getMediaUrl() { return mediaUrl; }
    public void setMediaUrl(String mediaUrl) { this.mediaUrl = mediaUrl; }

    public Integer getSequence() { return sequence; }
    public void setSequence(Integer sequence) { this.sequence = sequence; }

    public Comment getParentComment() { return parentComment; }
    public void setParentComment(Comment parentComment) { this.parentComment = parentComment; }

    public Set<Comment> getChildComments() { return childComments; }
    public void setChildComments(Set<Comment> childComments) { this.childComments = childComments; }

    public String getUserId() { return userId; }
    public void setUser(String userId) { this.userId = userId; }

    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }
}
