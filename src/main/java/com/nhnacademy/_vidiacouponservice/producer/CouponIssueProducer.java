package com.nhnacademy._vidiacouponservice.producer;

import com.nhnacademy._vidiacouponservice.config.RabbitMQConfig;
import com.nhnacademy._vidiacouponservice.domain.dto.CouponIssueMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 메시지를 MQ로 보내는놈
 * 흐름은 (Controller->Service->Producer->MQ) 이런느낌
 * 발급하는거를 비동기로 던질려고 쓰는거임
 */

@Component
@RequiredArgsConstructor
public class CouponIssueProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(CouponIssueMessage msg) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE,
            RabbitMQConfig.ROUTING_KEY,
            msg
        );
    }
}
