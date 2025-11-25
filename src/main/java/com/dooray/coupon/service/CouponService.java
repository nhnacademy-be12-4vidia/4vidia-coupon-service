package com.dooray.coupon.service;

import com.dooray.coupon.domain.CouponPolicy;
import com.dooray.coupon.domain.dto.CouponIssueMessage;
import com.dooray.coupon.producer.CouponIssueProducer;
import com.dooray.coupon.repository.CouponPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponPolicyRepository couponPolicyRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final CouponIssueProducer producer;

    /**
     * 발급요청->Redis재고 체크(메시지)->MQ에 집어넣기
     * 이게 지금 Redis DECR(원자적연산)임
     * @param userId
     * @param policyId
     */

    public void issue(Long userId, Long policyId) {

        CouponPolicy policy = couponPolicyRepository.findById(policyId)
                .orElseThrow(() -> new IllegalArgumentException("정책 없음"));

        if (!policy.getIsActivation())
            throw new IllegalStateException("비활성 정책");

        String key = "coupon:policy:" + policyId + ":stock";
        Long remain = redisTemplate.opsForValue().decrement(key);

        if (remain == null) throw new IllegalStateException("재고 정보 없음");
        if (remain < 0) throw new IllegalStateException("매진");

        producer.send(new CouponIssueMessage(userId, policyId));
    }


    public void issue2(Long userId, Long policyId) {
        CouponPolicy policy = couponPolicyRepository.findById(policyId)
                .orElseThrow(() -> new IllegalArgumentException("정책 없음"));

        if (!policy.getIsActivation())
            throw new IllegalStateException("비활성 정책");

        producer.send(new CouponIssueMessage(userId, policyId));
    }

}
