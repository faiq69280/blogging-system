package com.example.notification_service.channels;
import com.example.notification_service.constants.NotificationChannelType;
import com.example.notification_service.model.NotificationMessage;
import com.example.notification_service.model.NotificationResult;

public interface NotificationChannel {
    public NotificationResult sendMessage(NotificationMessage message);
    public boolean supports(NotificationChannelType notificationChannelType);
}
