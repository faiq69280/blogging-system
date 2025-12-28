package com.example.notification_service.event_listener;

import com.example.notification_service.constants.EventType;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import java.util.Arrays;
import java.util.List;

@Configuration
public class MessageQueueConfiguration {
    public static final String EXCHANGE = "app.events.x";
    public static final String QUEUE    = "app.events.consumer.q";
    public static List<String>  ALLOWED_TOPICS = Arrays.stream(EventType.values()).map(EventType::value).toList();

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public TopicExchange eventsExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue consumerQueue() {
        return QueueBuilder.durable(QUEUE).build();
    }

    // Multi-topic subscriptions (multiple bindings to the same queue)
    @Bean
    public Binding bindUserRegistered(Queue consumerQueue, TopicExchange eventsExchange) {
        return BindingBuilder.bind(consumerQueue).to(eventsExchange).with("user.registered");
    }

    @Bean
    public Binding bindPostLike(Queue consumerQueue, TopicExchange eventsExchange) {
        return BindingBuilder.bind(consumerQueue).to(eventsExchange).with("post.like");
    }

    @Bean
    public Binding bindPostComment(Queue consumerQueue, TopicExchange eventsExchange) {
        return BindingBuilder.bind(consumerQueue).to(eventsExchange).with("post.comment");
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                               MessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setDefaultRequeueRejected(false);
        factory.setMessageConverter(messageConverter);
        return factory;
    }
}
