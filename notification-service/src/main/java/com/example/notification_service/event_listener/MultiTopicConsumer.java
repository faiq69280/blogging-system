package com.example.notification_service.event_listener;

import com.example.notification_service.manager.NotificationSenderManager;
import com.example.notification_service.model.NotificationFailureResult;
import com.example.notification_service.model.NotificationResult;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.ImmediateRequeueAmqpException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class MultiTopicConsumer {

    @Autowired
    NotificationSenderManager notificationSenderManager;

    @RabbitListener(queues = MessageQueueConfiguration.QUEUE)
    public void onMessage(EventEnvelope event, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {

        if (!MessageQueueConfiguration.ALLOWED_TOPICS.contains(routingKey)) {
            throw new AmqpRejectAndDontRequeueException("Invalid routing key : %s, should be one of [%s]"
                    .formatted(routingKey, MessageQueueConfiguration.ALLOWED_TOPICS));
        }

        NotificationResult notificationResult = notificationSenderManager.sendNotification(event.payload());
        handleNotficationResult(notificationResult);
    }

    private NotificationResult handleNotficationResult(NotificationResult notificationResult) {
        if (notificationResult instanceof NotificationFailureResult nfe) {
            switch (nfe.getOutcomeType()) {
                case FAILURE ->
                        throw new AmqpRejectAndDontRequeueException("Unhandled failure, exiting with cause...%s".formatted(nfe.getPayload()),
                                nfe.getFailureRoot());
                case TRANSIENT_FAILURE ->
                        throw new ImmediateRequeueAmqpException("Retrying due to following transient failure : %s".formatted(nfe.getPayload()),
                                nfe.getFailureRoot());
            }
        }
        return notificationResult;
    }

}