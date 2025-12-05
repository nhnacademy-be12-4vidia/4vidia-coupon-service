package com.nhnacademy._vidiacouponservice.service;

import com.nhnacademy._vidiacouponservice.config.RedisKeys;
import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.common.ValidityType;
import com.nhnacademy._vidiacouponservice.domain.dto.request.CouponPolicyCreateRequest;
import com.nhnacademy._vidiacouponservice.domain.dto.request.CouponPolicyUpdateRequest;
import com.nhnacademy._vidiacouponservice.exception.PolicyNotFoundException;
import com.nhnacademy._vidiacouponservice.repository.CouponPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponPolicyService {

    private final CouponPolicyRepository policyRepo;
    private final RedisTemplate<String, String> redis;

    public CouponPolicy create(CouponPolicyCreateRequest req) {

        CouponPolicy policy = CouponPolicy.create(req);
        CouponPolicy saved = policyRepo.save(policy);

        String key = RedisKeys.policyHash(saved.getPolicyId());

        // 재고 저장
        redis.opsForHash().put(key, "stock",
                saved.getLimitedQuantity() == null ? "-1" : saved.getLimitedQuantity().toString());

        redis.opsForHash().put(key, "issued", "0");
        redis.opsForHash().put(key, "minOrderAmount", saved.getMinOrderAmount().toString());
        redis.opsForHash().put(key, "maxDiscountAmount", saved.getMaxDiscountAmount().toString());

        return saved;
    }

    public CouponPolicy update(Long policyId, CouponPolicyUpdateRequest req) {
        CouponPolicy policy = policyRepo.findById(policyId)
                .orElseThrow(() -> new PolicyNotFoundException(policyId));

        policy.update(req);
        return policyRepo.save(policy);
    }

    private void validatePolicyRequest(
            ValidityType validityType,
            Integer validDays,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        if (validityType == ValidityType.RELATIVE) {
            if (validDays == null || validDays <= 0) {
                throw new IllegalArgumentException("RELATIVE 정책은 validDays가 반드시 필요합니다.");
            }
        } else { // ABSOLUTE
            if (startDate == null || endDate == null) {
                throw new IllegalArgumentException("ABSOLUTE 정책은 startDate/endDate가 반드시 필요합니다.");
            }
        }
    }

    public List<CouponPolicy> findAllActive() {
        return policyRepo.findAllByIsActivationTrue();
    }

    public CouponPolicy find(Long policyId) {
        return policyRepo.findById(policyId)
                .orElseThrow(() -> new PolicyNotFoundException(policyId));
    }
}
