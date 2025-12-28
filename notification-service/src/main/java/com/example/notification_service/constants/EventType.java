package com.example.notification_service.constants;

public enum EventType {
    USER_REGISTERED("user.registered"),
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
