package com.example.article_api.async.message_queue;

import com.example.article_api.async.events.EventEnvelope;

public interface MessageQueueProducer {
    public void sendMessage(EventEnvelope eventEnvelope, String topic);
}
