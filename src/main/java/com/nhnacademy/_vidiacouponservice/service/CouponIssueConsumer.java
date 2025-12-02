package com.nhnacademy._vidiacouponservice.service;


import com.nhnacademy._vidiacouponservice.config.RabbitMQConfig;
import com.nhnacademy._vidiacouponservice.config.RedisKeys;
import com.nhnacademy._vidiacouponservice.domain.Coupon;
import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.UserCoupon;
import com.nhnacademy._vidiacouponservice.domain.common.CouponStatus;
import com.nhnacademy._vidiacouponservice.domain.common.ValidityType;
import com.nhnacademy._vidiacouponservice.domain.dto.CouponIssueMessage;
import com.nhnacademy._vidiacouponservice.exception.PolicyInactiveException;
import com.nhnacademy._vidiacouponservice.repository.CouponPolicyRepository;
import com.nhnacademy._vidiacouponservice.repository.CouponRepository;
import com.nhnacademy._vidiacouponservice.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * MQ에서 메시지 하나씩 소비함
 * 얘가 진짜 쿠폰을 DB에 저장
 * 쿠폰 발급 이벤트를 처리하는 Consumer.
 * topic: coupon.issue.# 에 묶인 큐에서 메시지를 소비한다.
 */


@Component
@RequiredArgsConstructor
public class CouponIssueConsumer {

    private final CouponPolicyRepository policyRepo;
    private final CouponRepository couponRepo;
    private final UserCouponRepository userCouponRepo;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 쿠폰생성+UserCoupon생성+정책 발급량 증가
     * 큐에서 메시지 하나씩 꺼내서 DB 저장
     * 선착순쿠폰만 해당하는거임
     * 1: 정책조회
     * 2: 쿠폰엔티티 생성
     * 3: UserCoupon 생성
     * 4: 정책 issuedQuantity(쿠폰발행수) 증가
     * 234중에 하나라도 실패하면 쿠폰은 생성될수가 없음
     */
    @RabbitListener(queues = RabbitMQConfig.ISSUE_QUEUE)
    @Transactional
    public void onMessage(CouponIssueMessage msg) {

        try {

            CouponPolicy policy = policyRepo.findById(msg.policyId())
                    .orElseThrow(() -> new PolicyInactiveException(msg.policyId()));


            // 활성화 X → 발급 안됨
            if (!policy.getIsActivation()) {
                throw new PolicyInactiveException(msg.policyId());
            }

            // 2) 발급 시점 / 만료일 계산
            LocalDateTime issuedAt = LocalDateTime.now();
            LocalDateTime expireAt = calcExpire(policy, issuedAt);

            //쿠폰생성
            Coupon coupon = new Coupon();
            coupon.setCouponPolicy(policy);
            coupon.setIssuedAt(issuedAt);
            coupon.setExpireAt(expireAt);
            coupon.setStatus(CouponStatus.UNUSED);
            couponRepo.save(coupon);

            // user_coupon생성
            userCouponRepo.save(new UserCoupon(msg.userId(), coupon));  // 여기서 중복으로 실패 가능

            // 4) 정책 발급량 증가(DB)
            policy.setIssuedQuantity(policy.getIssuedQuantity() + 1);
            policyRepo.save(policy);

            // 5) Redis issued 증가
            redisTemplate.opsForHash().increment(
                    RedisKeys.policyHash(msg.policyId()),
                    "issued",
                    1
            );
        } catch (DataIntegrityViolationException e) {
            // user_coupon UNIQUE 중복 시 → Redis 재고 복구
            redisTemplate.opsForHash().increment(
                    RedisKeys.policyHash(msg.policyId()),
                    "stock",
                    1
            );

            throw e;
        }
    }


    private LocalDateTime calcExpire(CouponPolicy p, LocalDateTime issuedAt) {
        if (p.getValidityType() == ValidityType.RELATIVE)
            return issuedAt.plusDays(p.getValidDays());
        return p.getEndDate();
    }
}