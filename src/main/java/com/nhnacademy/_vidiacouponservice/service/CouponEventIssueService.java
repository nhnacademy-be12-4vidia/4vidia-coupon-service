package com.nhnacademy._vidiacouponservice.service;

import com.nhnacademy._vidiacouponservice.config.RedisKeys;
import com.nhnacademy._vidiacouponservice.client.UserClient;
import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.exception.DuplicateIssueRequestException;
import com.nhnacademy._vidiacouponservice.exception.PolicyInactiveException;
import com.nhnacademy._vidiacouponservice.exception.PolicyNotFoundException;
import com.nhnacademy._vidiacouponservice.producer.CouponIssueProducer;
import com.nhnacademy._vidiacouponservice.repository.CouponPolicyRepository;
import com.nhnacademy._vidiacouponservice.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponEventIssueService {

    private final CouponPolicyRepository policyRepo;
    private final UserCouponRepository userCouponRepo;
    private final RedisTemplate<String, String> redis;
    private final UserClient userClient;
    private final CouponIssueProducer producer;

    public void issueEventCoupon(Long userId, Long policyId) {

        userClient.validateUser(userId);

        CouponPolicy policy = policyRepo.findById(policyId)
                .orElseThrow(() -> new PolicyNotFoundException(policyId));

        if (!policy.getIsActivation())
            throw new PolicyInactiveException(policyId);

        // 단일 발급 정책(웰컴/생일 등)
        if (userCouponRepo.existsByIdUserIdAndPolicyId(userId, policyId)) {
            throw new DuplicateIssueRequestException(userId, policyId);
        }

        // 중복 요청 방지
        String dupKey = RedisKeys.dupKey(policyId, userId);
        Boolean ok = redis.opsForValue().setIfAbsent(dupKey, "1");
        if (Boolean.FALSE.equals(ok))
            throw new DuplicateIssueRequestException(userId, policyId);
        redis.expire(dupKey, Duration.ofSeconds(60));

        LocalDateTime issuedAt = LocalDateTime.now();
        LocalDateTime expireAt = ExpireCalculator.calcExpiry(policy, issuedAt);

        producer.sendEvent(userId, policyId, issuedAt, expireAt);
    }
}
