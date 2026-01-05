package com.nhnacademy._vidiacouponservice.config;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class RabbitMQConfigTest {

    @Autowired
    TopicExchange couponExchange;

    @Autowired
    Queue issueQueue;

    @Autowired
    Queue eventQueue;

    @Autowired
    Queue deadLetterQueue;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    @DisplayName("쿠폰용 TopicExchange가 정상적으로 생성")
    void exchangeBean_shouldBeCreated() {
        assertThat(couponExchange.getName()).isEqualTo("coupon4.exchange");
    }

    @Test
    @DisplayName("쿠폰 발급, 이벤트, DLQ 큐가 정상적으로 생성")
    void queues_shouldBeCreated() {
        assertThat(issueQueue.getName()).isEqualTo("coupon4.issue.queue");
        assertThat(eventQueue.getName()).isEqualTo("coupon4.event.queue");
        assertThat(deadLetterQueue.getName()).isEqualTo("coupon4.issue.dlq");
    }

    @Test
    @DisplayName("RabbitTemplate은 JSON 메시지 컨버터를 사용")
    void rabbitTemplate_shouldHaveMessageConverter() {
        assertThat(rabbitTemplate).isNotNull();
        assertThat(rabbitTemplate.getMessageConverter()).isNotNull();
    }
}
