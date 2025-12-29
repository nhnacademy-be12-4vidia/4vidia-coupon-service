package com.nhnacademy._vidiacouponservice.producer;

import com.nhnacademy._vidiacouponservice.config.RabbitMQConfig;
import com.nhnacademy._vidiacouponservice.domain.dto.CouponIssueMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 메시지를 MQ로 보내는놈
 * 흐름은 (Controller->Service->Producer->MQ) 이런느낌
 * 발급하는거를 비동기로 던질려고 쓰는거임
 * 쿠폰 발급 요청 메시지를 MQ로 보내는 Producer.
 * topic routing key: coupon.issue.requested
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CouponIssueProducer {

    private final RabbitTemplate rabbit;

    public void sendIssue(Long userId, Long policyId, LocalDateTime issuedAt, LocalDateTime expireAt) {

        CouponIssueMessage msg = new CouponIssueMessage(userId, policyId, issuedAt, expireAt);

        rabbit.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                "coupon4.issue.requested",
                msg
        );
    }

    public void sendEvent(Long userId, Long policyId, LocalDateTime issuedAt, LocalDateTime expireAt) {
        CouponIssueMessage msg = new CouponIssueMessage(userId, policyId, issuedAt, expireAt);
        rabbit.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                "coupon4.event.requested",
                msg
        );
    }

    public void sendRollBack(Long orderId) {
        rabbit.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROLLBACK_ROUTING_KEY,
                String.valueOf(orderId)
        );
    }
}
