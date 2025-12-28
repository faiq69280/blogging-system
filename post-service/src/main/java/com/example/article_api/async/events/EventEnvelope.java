package com.example.article_api.async.events;

public record EventEnvelope(String eventId,
                            EventType eventType,
                            MessagePayload payload) {
}
