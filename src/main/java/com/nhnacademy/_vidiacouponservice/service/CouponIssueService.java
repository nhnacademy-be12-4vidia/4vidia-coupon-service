package com.nhnacademy._vidiacouponservice.service;

import com.nhnacademy._vidiacouponservice.config.RedisKeys;
import com.nhnacademy._vidiacouponservice.client.UserClient;
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
    private final UserClient userClient;

    public void issue(Long userId, Long policyId) {

        userClient.validateUser(userId);

        CouponPolicy policy = repo.findById(policyId)
                .orElseThrow(() -> new PolicyNotFoundException(policyId));

        // 🔒 생일/웰컴 정책 차단
        if (policy.getPolicyType() == PolicyType.WELCOME
                || policy.getPolicyType() == PolicyType.BIRTHDAY) {
            throw new PolicyNotAdminIssuableException(policyId);
        }

        if (!policy.getIsActivation())
            throw new PolicyInactiveException(policyId);

        // 1) 중복 발급 방지 (SETNX)
        String dupKey = RedisKeys.dupKey(policyId, userId);
        Boolean ok = redis.opsForValue().setIfAbsent(dupKey, "1");

        if (Boolean.FALSE.equals(ok))
            throw new DuplicateIssueRequestException(userId, policyId);

        redis.expire(dupKey, Duration.ofSeconds(60));

        // 재고감소는 컨슈머에서

        // 3) 발급/만료시간 계산
        LocalDateTime issuedAt = LocalDateTime.now();
        LocalDateTime expireAt = ExpireCalculator.calcExpiry(policy, issuedAt);

        // 4) MQ 발송
        producer.sendIssue(userId, policyId, issuedAt, expireAt);
    }
}
