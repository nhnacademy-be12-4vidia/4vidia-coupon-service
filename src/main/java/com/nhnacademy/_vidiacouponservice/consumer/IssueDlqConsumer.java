package com.nhnacademy._vidiacouponservice.consumer;

import com.nhnacademy._vidiacouponservice.config.RedisKeys;
import com.nhnacademy._vidiacouponservice.domain.dto.CouponIssueMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IssueDlqConsumer {

    private final RedisTemplate<String, String> redis;

    @RabbitListener(queues = "coupon4.issue.dlq")
    public void handleDlq(CouponIssueMessage msg) {

        String key = RedisKeys.policyHash(msg.policyId());

        // MQ 발급 실패 → 재고 복구
        redis.opsForHash().increment(key, "stock", 1);
    }
}
