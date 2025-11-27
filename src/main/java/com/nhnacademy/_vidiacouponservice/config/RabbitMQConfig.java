package com.nhnacademy._vidiacouponservice.config;



import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "coupon4.exchange";

    public static final String ISSUE_QUEUE = "coupon4.issue.queue";
    public static final String STOCK_QUEUE = "coupon4.stock.queue";


    @Bean
    public TopicExchange couponExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue issueQueue() {
        return new Queue(ISSUE_QUEUE, true);
    }

    @Bean
    public Queue stockQueue() {
        return new Queue(STOCK_QUEUE, true);
    }

    @Bean
    public Binding issueBinding() {
        return BindingBuilder
                .bind(issueQueue())
                .to(couponExchange())
                .with("coupon4.issue.#");
    }

    @Bean
    public Binding stockBinding() {
        return BindingBuilder
                .bind(stockQueue())
                .to(couponExchange())
                .with("coupon4.stock.#");
    }
}
