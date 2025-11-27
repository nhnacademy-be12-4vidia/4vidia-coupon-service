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

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponPolicyRepository repo;
    private final RedisTemplate<String, String> redisTemplate;
    private final CouponIssueProducer producer;

    public void issue(Long userId, Long policyId) {
        CouponPolicy policy = repo.findById(policyId)
                .orElseThrow(() -> new PolicyNotFoundException(policyId));

        if (!policy.getIsActivation()) throw new PolicyInactiveException(policyId);

        //중복 발급 방지
        String key = "coupon:policy:" + policyId + ":stock";
        Long remain = redisTemplate.opsForValue().decrement(key);

        if (remain == null) throw new PolicyStockMissingException(policyId);
        if (remain < 0) throw new PolicyOutOfStockException(policyId);

        producer.send(new CouponIssueMessage(userId, policyId));
    }

    public void issue2(Long userId, Long policyId) {
        CouponPolicy policy = repo.findById(policyId)
                .orElseThrow(() -> new PolicyNotFoundException(policyId));

        if (!policy.getIsActivation()) throw new PolicyInactiveException(policyId);

        producer.send(new CouponIssueMessage(userId, policyId));
    }
}
