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

        validatePolicyRequest(
                req.validityType(),
                req.validDays(),
                req.startDate(),
                req.endDate()
        );

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

        // 수정 가능한 값만 변경
        policy.setPolicyName(req.policyName());
        policy.setDiscountType(req.discountType());
        policy.setDiscountValue(req.discountValue());
        policy.setDiscountTargetType(req.discountTargetType());
        policy.setCategoryId(req.categoryId());
        policy.setBookId(req.bookId());
        policy.setMinOrderAmount(req.minOrderAmount());
        policy.setMaxDiscountAmount(req.maxDiscountAmount());

        return policyRepo.save(policy);
    }



    private void validatePolicyRequest(
            ValidityType validityType,
            Integer validDays,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {

        if (validityType == ValidityType.RELATIVE) {

            // 1) validDays 필수
            if (validDays == null || validDays <= 0) {
                throw new IllegalArgumentException("RELATIVE 정책은 validDays가 필수입니다.");
            }

            // 2) ABSOLUTE 값 들어오면 안 됨
            if (startDate != null || endDate != null) {
                throw new IllegalArgumentException("RELATIVE 정책에서는 startDate/endDate를 설정할 수 없습니다.");
            }

        } else if (validityType == ValidityType.ABSOLUTE) {

            // 1) start/endDate 필수
            if (startDate == null || endDate == null) {
                throw new IllegalArgumentException("ABSOLUTE 정책은 startDate와 endDate가 모두 필요합니다.");
            }

            // 2) RELATIVE 값 들어오면 안 됨
            if (validDays != null) {
                throw new IllegalArgumentException("ABSOLUTE 정책에서는 validDays를 설정할 수 없습니다.");
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

    public void activate(Long id) {
        CouponPolicy p = policyRepo.findById(id)
                .orElseThrow(() -> new PolicyNotFoundException(id));
        p.setIsActivation(true);
        policyRepo.save(p);
    }

    public void deactivate(Long id) {
        CouponPolicy p = policyRepo.findById(id)
                .orElseThrow(() -> new PolicyNotFoundException(id));
        p.setIsActivation(false);
        policyRepo.save(p);
    }

}
