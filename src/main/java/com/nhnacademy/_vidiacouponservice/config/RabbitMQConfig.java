package com.nhnacademy._vidiacouponservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AutoConfigureOrder(0)
@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "coupon4.exchange";

    // 정상 처리 queue
    public static final String ISSUE_QUEUE = "coupon4.issue.queue";
    public static final String EVENT_QUEUE = "coupon4.event.queue";

    // DLQ
    public static final String DEAD_LETTER_QUEUE = "coupon4.issue.dlq";

    // routing keys
    public static final String ISSUE_ROUTING_KEY = "coupon4.issue.requested";
    public static final String EVENT_ROUTING_KEY = "coupon4.event.requested";

    public static final String ISSUE_DLX_ROUTING_KEY = "coupon4.issue.failed";

    public static final String ROLLBACK_QUEUE = "coupon4.rollback.queue";
    public static final String ROLLBACK_ROUTING_KEY = "coupon4.use.rollback";

    @Bean
    public TopicExchange couponExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue issueQueue() {
        return QueueBuilder.durable(ISSUE_QUEUE)
                .withArgument("x-dead-letter-exchange", EXCHANGE)
                .withArgument("x-dead-letter-routing-key", ISSUE_DLX_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue eventQueue() {
        return QueueBuilder.durable(EVENT_QUEUE)
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    @Bean
    public Binding issueBinding() {
        return BindingBuilder
                .bind(issueQueue())
                .to(couponExchange())
                .with(ISSUE_ROUTING_KEY);
    }

    @Bean
    public Binding eventBinding() {
        return BindingBuilder
                .bind(eventQueue())
                .to(couponExchange())
                .with(EVENT_ROUTING_KEY);
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder
                .bind(deadLetterQueue())
                .to(couponExchange())
                .with(ISSUE_DLX_ROUTING_KEY);
    }

    @Bean
    public Queue rollbackQueue() {
        return QueueBuilder.durable(ROLLBACK_QUEUE).build();
    }

    @Bean
    public Binding rollbackBinding(@Qualifier("rollbackQueue") Queue rollbackQueue,
                                   TopicExchange couponExchange) {
        return BindingBuilder.bind(rollbackQueue)
                .to(couponExchange)
                .with(ROLLBACK_ROUTING_KEY);
    }


    @Bean
    public MessageConverter jacksonMessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter converter
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(converter); // ★ 핵심
        return factory;
    }

}