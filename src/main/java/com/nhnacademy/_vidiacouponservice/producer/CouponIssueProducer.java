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
 * 쿠폰 발급 요청 메시지를 MQ로 보내는 Producer.
 * topic routing key: coupon.issue.requested
 */
@Component
@RequiredArgsConstructor
public class CouponIssueProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(String routingKey, CouponIssueMessage msg) {
        /**
         * rabbitTemplate.convertAndSend
         * 얘 인자를 3개를 받음
         * exchange 이름 -> 메시지를 최종큐 보내기전에 허브로 먼저감
         * routingKey -> 어떤 큐로 갈지 결정하는 기준
         * 큐에 실제로 들어갈 데이터(우리는 userId, policyId)가 들어있는 레코드
         */
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                routingKey,
                msg
        );
    }
    /**
     * 대충 뭔소리냐
     * 쿠폰을 발급 요청하면
     * 제일먼저 허브(exchange)로 보냄
     * 그리고 routingKey로 issue, stock, events를 분류함
     * 그리고 슛
     */
}