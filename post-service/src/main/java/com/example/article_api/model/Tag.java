package com.example.article_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Entity
@Table
@Builder
@AllArgsConstructor
public class Tag extends BaseEntity {
    @Column(unique = true)
    private String tagText;

    @ManyToMany(mappedBy = "tags",fetch = FetchType.LAZY)
    List<Post> posts;

    public Tag() {}
    public String getTagText() { return tagText; }
    public void setTagText(String tagText) { this.tagText = tagText; }

    public List<Post> getPosts() { return posts; }
    public void setPosts(List<Post> posts) { this.posts = posts; }
}
