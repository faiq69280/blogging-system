package com.example.article_api.async.message_queue;


import com.example.article_api.async.events.EventEnvelope;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQProducer implements MessageQueueProducer {
    RabbitTemplate rabbitTemplate;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * @param eventEnvelope
     */
    @Override
    public void sendMessage(EventEnvelope eventEnvelope, String topic) {
         rabbitTemplate.convertAndSend(RabbitMQProducerConfig.EXCHANGE, topic, eventEnvelope);
    }
}
