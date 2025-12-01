package com.nhnacademy._vidiacouponservice.service;



import com.nhnacademy._vidiacouponservice.config.RedisKeys;
import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.dto.CouponPolicyCreateRequest;
import com.nhnacademy._vidiacouponservice.domain.dto.CouponPolicyUpdateRequest;
import com.nhnacademy._vidiacouponservice.exception.PolicyNotFoundException;
import com.nhnacademy._vidiacouponservice.repository.CouponPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponPolicyService {

    private final CouponPolicyRepository couponPolicyRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public CouponPolicy createPolicy(CouponPolicyCreateRequest dto) {
        CouponPolicy policy = CouponPolicy.create(dto);
        CouponPolicy saved = couponPolicyRepository.save(policy);

        // 🔥 Redis Hash로 저장
        String key = RedisKeys.policyHash(saved.getPolicyId());
        redisTemplate.opsForHash().put(key, "stock", saved.getLimitedQuantity());
        redisTemplate.opsForHash().put(key, "issued", 0);
        redisTemplate.opsForHash().put(key, "maxDiscountAmount", saved.getMaxDiscountAmount());

        return saved;
    }

    public CouponPolicy updatePolicy(Long id, CouponPolicyUpdateRequest dto) {
        CouponPolicy policy = findPolicy(id);
        policy.update(dto);
        return policy;
    }

    @Transactional(readOnly = true)
    public Iterable<CouponPolicy> listPolicies() {
        return couponPolicyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public CouponPolicy findPolicy(Long id) {
        return couponPolicyRepository.findById(id).orElseThrow(() -> new PolicyNotFoundException(id));
    }

    public void deactivatePolicy(Long id) {
        CouponPolicy policy = findPolicy(id);
        policy.setIsActivation(false);
    }

    public void changeActivePolicy(Long id, boolean active) {
        CouponPolicy p = findPolicy(id);
        p.setIsActivation(active);
    }

}
