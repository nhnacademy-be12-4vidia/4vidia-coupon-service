package com.nhnacademy._vidiacouponservice.service;

import com.nhnacademy._vidiacouponservice.config.RedisKeys;
import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.common.PolicyType;
import com.nhnacademy._vidiacouponservice.exception.*;
import com.nhnacademy._vidiacouponservice.producer.CouponIssueProducer;
import com.nhnacademy._vidiacouponservice.repository.CouponPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponIssueService {

    private final CouponPolicyRepository repo;
    private final RedisTemplate<String, String> redis;
    private final CouponIssueProducer producer;

    public void issue(Long userId, Long policyId) {

        CouponPolicy policy = repo.findById(policyId)
                .orElseThrow(() -> new PolicyNotFoundException(policyId));

        // 🔒 이벤트/웰컴 정책 차단
        if (policy.getPolicyType() == PolicyType.WELCOME
                || policy.getPolicyType() == PolicyType.BIRTHDAY) {
            throw new IllegalArgumentException(
                    "해당 정책은 관리자 선착순 발급 대상이 아닙니다."
            );
        }

        if (!policy.getIsActivation())
            throw new PolicyInactiveException(policyId);

        // 1) 중복 발급 방지 (SETNX)
        String dupKey = RedisKeys.dupKey(policyId, userId);
        Boolean ok = redis.opsForValue().setIfAbsent(dupKey, "1");

        if (Boolean.FALSE.equals(ok))
            throw new IllegalArgumentException("이미 발급 요청 처리중입니다.");

        redis.expire(dupKey, Duration.ofSeconds(60));

        // 2) 재고 감소
        String hashKey = RedisKeys.policyHash(policyId);
        Long remain = redis.opsForHash().increment(hashKey, "stock", -1);

        if (remain == null)
            throw new PolicyStockMissingException(policyId);

        if (remain < 0)
            throw new PolicyOutOfStockException(policyId);

        // 3) 발급/만료시간 계산
        LocalDateTime issuedAt = LocalDateTime.now();
        LocalDateTime expireAt = ExpireCalculator.calcExpiry(policy, issuedAt);

        // 4) MQ 발송
        producer.sendIssue(userId, policyId, issuedAt, expireAt);
    }
}
