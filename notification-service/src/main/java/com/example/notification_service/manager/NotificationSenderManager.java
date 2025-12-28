package com.example.notification_service.manager;

import com.example.notification_service.channels.NotificationChannel;
import com.example.notification_service.constants.OutcomeType;
import com.example.notification_service.model.NotificationMessage;
import com.example.notification_service.model.NotificationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class NotificationSenderManager {
    private static final Logger log = LoggerFactory.getLogger(NotificationSenderManager.class);
    List<NotificationChannel> senderDelegates;

    public NotificationSenderManager(List<NotificationChannel> senderDelegates) {
        this.senderDelegates = senderDelegates;
    }

    /**
     * A central manager that delegates responsibility of sending notification to the appropriate channel.
     * @param notificationMessage
     * @return
     */
    public NotificationResult sendNotification(NotificationMessage notificationMessage) {
        if (notificationMessage == null) {
            throw new IllegalArgumentException("Null notification message provided!");
        }

        NotificationResult result = null;
        for (NotificationChannel channel : senderDelegates) {
            if (channel.supports(notificationMessage.getNotificationChannelType())) {
                result = channel.sendMessage(notificationMessage);

                if (result.getOutcomeType().equals(OutcomeType.SUCCESS)) {
                    return result;
                }
            }
        }

        return result;
    }
}
