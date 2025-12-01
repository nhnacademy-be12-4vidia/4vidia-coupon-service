package com.nhnacademy._vidiacouponservice.config;



import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "coupon4.exchange";

    // 선착순
    public static final String ISSUE_QUEUE = "coupon4.issue.queue";
    // 재고의 상한이 있는 이벤트쿠폰
    public static final String STOCK_QUEUE = "coupon4.stock.queue";
    // birthday/welcome 같은 재고없는 쿠폰
    public static final String EVENT_QUEUE = "coupon4.events.queue";


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
    public Queue eventQueue() {
        return new Queue(EVENT_QUEUE, true);
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

    @Bean
    public Binding eventBinding() {
        return BindingBuilder
                .bind(eventQueue())
                .to(couponExchange())
                .with("coupon4.events.*");
    }
}
