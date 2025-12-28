package com.example.article_api.async.events;
public enum EventType {
    POST_LIKED("post.like"),
    POST_COMMENT("post.comment");

    private final String value;

    private EventType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
