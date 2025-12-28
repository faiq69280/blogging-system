package com.example.article_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class UploadedMedia {
    String url;
    long size;
    Post.PostMediaType mediaType;

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public long getSize() { return size; }
    public void setSize(long size) { this.size = size; }

    public Post.PostMediaType getMediaType() { return mediaType; }
    public void setMediaType(Post.PostMediaType mediaType) { this.mediaType = mediaType; }
}
