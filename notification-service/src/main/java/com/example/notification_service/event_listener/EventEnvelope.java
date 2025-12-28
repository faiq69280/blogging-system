package com.example.notification_service.event_listener;

import com.example.notification_service.constants.EventType;
import com.example.notification_service.model.NotificationMessage;

public record EventEnvelope(String eventId,
                            EventType eventType,
                            NotificationMessage payload) {
}
