package com.nhnacademy._vidiacouponservice.config;



import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "coupon4.exchange";

    // 선착순
    public static final String ISSUE_QUEUE = "coupon4.issue.queue";
    public static final String ISSUE_DLQ = "coupon4.issue.dlq";
    public static final String ISSUE_DLX = "coupon4.issue.dlx";

    // 재고의 상한이 있는 이벤트쿠폰
    public static final String STOCK_QUEUE = "coupon4.stock.queue";
    // birthday/welcome 같은 재고없는 쿠폰
    public static final String EVENT_QUEUE = "coupon4.events.queue";


    @Bean
    public TopicExchange couponExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public DirectExchange issueDlx() {
        return new DirectExchange(ISSUE_DLX);
    }

    @Bean
    public Queue issueDlq() {
        return QueueBuilder.durable(ISSUE_DLQ).build();
    }



    @Bean
    public Queue issueQueue() {
        return QueueBuilder.durable(ISSUE_QUEUE)
                .withArgument("x-dead-letter-exchange", ISSUE_DLX)
                .withArgument("x-dead-letter-routing-key", "dlq")
                .build();
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
    public Binding issueDlqBinding() {
        return BindingBuilder
                .bind(issueDlq())
                .to(issueDlx())
                .with("dlq");
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
