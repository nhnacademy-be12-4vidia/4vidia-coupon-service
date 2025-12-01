package com.nhnacademy._vidiacouponservice.service;

import com.nhnacademy._vidiacouponservice.config.RabbitMQConfig;
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

    private final RedisTemplate<String, String> redisTemplate;

    @RabbitListener(queues = RabbitMQConfig.ISSUE_DLQ)
    public void onDlqMessage(CouponIssueMessage msg) {
        
        String hashKey = RedisKeys.policyHash(msg.policyId());

        redisTemplate.opsForHash().increment(hashKey, "stock", 1);

        log.warn("쿠폰 발급 실패 보상 처리 완료: policy={}, user={}", 
                 msg.policyId(), msg.userId());
    }
}
