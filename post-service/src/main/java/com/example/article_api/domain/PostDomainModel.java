package com.example.article_api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Builder
@AllArgsConstructor
public class PostDomainModel {
    private String postId;
    private String name;
    private String caption;
    private List<String> tags;
    private String url;

    private PostMetadata postMetadata;

    public PostDomainModel(String postId, String name, String caption, List<String> tags, String url) {
        this.postId = postId;
        this.name = name;
        this.caption = caption;
        this.tags = tags;
        this.url = url;
    }

    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public PostMetadata getPostMetadata() { return postMetadata; }
    public void setPostMetadata(PostMetadata postMetadata) { this.postMetadata = postMetadata; }
}
