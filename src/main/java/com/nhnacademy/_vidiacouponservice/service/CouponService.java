package com.nhnacademy._vidiacouponservice.service;

import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.dto.CouponIssueMessage;
import com.nhnacademy._vidiacouponservice.exception.PolicyInactiveException;
import com.nhnacademy._vidiacouponservice.exception.PolicyNotFoundException;
import com.nhnacademy._vidiacouponservice.exception.PolicyOutOfStockException;
import com.nhnacademy._vidiacouponservice.exception.PolicyStockMissingException;
import com.nhnacademy._vidiacouponservice.producer.CouponIssueProducer;
import com.nhnacademy._vidiacouponservice.repository.CouponPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponPolicyRepository repo;
    private final RedisTemplate<String, String> redisTemplate;
    private final CouponIssueProducer producer;
    
    // 선착순
    public void issue(Long userId, Long policyId) {
        CouponPolicy policy = repo.findById(policyId)
                .orElseThrow(() -> new PolicyNotFoundException(policyId));

        if (!policy.getIsActivation())
            throw new PolicyInactiveException(policyId);

        // 중복 발급 방지(SETNX(selfIfAbsent) + EXPIRE)
        String dupKey = "coupon:dup:" + policyId + ":" + userId;
        // 키없으면 키만들고 true : 키있으면 false
        Boolean ok = redisTemplate.opsForValue().setIfAbsent(dupKey, "1");
        if (Boolean.FALSE.equals(ok)) {
            throw new IllegalArgumentException("이미 발급 요청 처리중입니다.");
        }
        // 분산 락느낌 -> MQ처리중에 서버뒤지거나, consumer실패해도 60초지나면 잠금해제
        redisTemplate.expire(dupKey, Duration.ofSeconds(60));

        // 재고 감소(DECR)
        String stockKey = "coupon:policy:" + policyId + ":stock";
        Long remain = redisTemplate.opsForValue().decrement(stockKey);

        if (remain == null) throw new PolicyStockMissingException(policyId);
        if (remain < 0) throw new PolicyOutOfStockException(policyId); // 재고 초과발급시도

        // routing key 명시
        producer.send("coupon4.issue.requested", new CouponIssueMessage(userId, policyId));
    }


    // birthday, welcome, 재고없는거
    public void issue2(Long userId, Long policyId) {
        CouponPolicy policy = repo.findById(policyId)
                .orElseThrow(() -> new PolicyNotFoundException(policyId));

        if (!policy.getIsActivation())
            throw new PolicyInactiveException(policyId);

        // 중복 발급 방지
        String dupKey = "coupon:dup:" + policyId + ":" + userId;
        Boolean ok = redisTemplate.opsForValue().setIfAbsent(dupKey, "1");
        if (Boolean.FALSE.equals(ok)) {
            throw new IllegalArgumentException("이미 발급 요청 처리중입니다.");
        }
        redisTemplate.expire(dupKey, Duration.ofSeconds(60));


        // 이벤트 발급은 재고 안 줄임
        producer.send("coupon4.events.requested", new CouponIssueMessage(userId, policyId));
    }
}
