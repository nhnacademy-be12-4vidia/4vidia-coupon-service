package com.nhnacademy._vidiacouponservice.consumer;

import com.nhnacademy._vidiacouponservice.config.RedisKeys;
import com.nhnacademy._vidiacouponservice.domain.Coupon;
import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.UserCoupon;
import com.nhnacademy._vidiacouponservice.domain.common.CouponStatus;
import com.nhnacademy._vidiacouponservice.domain.dto.CouponIssueMessage;
import com.nhnacademy._vidiacouponservice.domain.dto.RollbackCouponMessage;
import com.nhnacademy._vidiacouponservice.exception.CouponNotHoldException;
import com.nhnacademy._vidiacouponservice.exception.PolicyNotFoundException;
import com.nhnacademy._vidiacouponservice.repository.CouponPolicyRepository;
import com.nhnacademy._vidiacouponservice.repository.CouponRepository;
import com.nhnacademy._vidiacouponservice.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * MQ에서 메시지 하나씩 소비함
 * 얘가 진짜 쿠폰을 DB에 저장
 * 쿠폰 발급 이벤트를 처리하는 Consumer.
 * topic: coupon.issue.# 에 묶인 큐에서 메시지를 소비한다.
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponIssueConsumer {

    private final CouponRepository couponRepo;
    private final UserCouponRepository userCouponRepo;
    private final CouponPolicyRepository policyRepo;
    private final RedisTemplate<String, String> redis;

    @RabbitListener(queues = "coupon4.issue.queue",
            containerFactory = "rabbitListenerContainerFactory")
    @Transactional
    public void consume(CouponIssueMessage msg) {

        CouponPolicy policy = policyRepo.findById(msg.policyId())
                .orElseThrow(() -> new IllegalArgumentException("정책 없음"));

        Coupon coupon = new Coupon();
        coupon.setCouponPolicy(policy);
        coupon.setIssuedAt(msg.issuedAt());
        coupon.setExpireAt(msg.expireAt());
        coupon.setStatus(CouponStatus.UNUSED);

        couponRepo.save(coupon);

        // user_coupon 생성
        userCouponRepo.save(new UserCoupon(msg.userId(), coupon));

        try {
            // 이미 user_coupon이 존재하면 UNIQUE 에러 발생 → catch로 가게 됨
            userCouponRepo.save(new UserCoupon(msg.userId(), coupon));
        } catch (DataIntegrityViolationException e) {
            // 여기서 무시하지 않으면 MQ는 재시도 → 무한재시도 지옥 발생 가능
            log.warn("중복 발급 메시지 감지 → 무시함 user={}, policy={}", msg.userId(), msg.policyId());
            return;
        }

        // issued_quantity += 1
        policy.increaseIssuedQuantity();
        policyRepo.save(policy);

        // Redis 발급 카운트 증가
        redis.opsForHash().increment(RedisKeys.policyHash(msg.policyId()), "issued", 1);
    }

    // 웰컴/생일/이벤트 (재고 무제한)
    @RabbitListener(queues = "coupon4.event.queue",
            containerFactory = "rabbitListenerContainerFactory")
    @Transactional
    public void consumeEvent(CouponIssueMessage msg) {

        CouponPolicy policy = policyRepo.findById(msg.policyId())
                .orElseThrow(() -> new IllegalArgumentException("정책 없음"));

        Coupon coupon = new Coupon();
        coupon.setCouponPolicy(policy);
        coupon.setIssuedAt(msg.issuedAt());
        coupon.setExpireAt(msg.expireAt());
        coupon.setStatus(CouponStatus.UNUSED);

        couponRepo.save(coupon);
        userCouponRepo.save(new UserCoupon(msg.userId(), coupon));

        try {
            // 이미 user_coupon이 존재하면 UNIQUE 에러 발생 → catch로 가게 됨
            userCouponRepo.save(new UserCoupon(msg.userId(), coupon));
        } catch (DataIntegrityViolationException e) {
            // 여기서 무시하지 않으면 MQ는 재시도 → 무한재시도 지옥 발생 가능
            log.warn("중복 발급 메시지 감지 → 무시함 user={}, policy={}", msg.userId(), msg.policyId());
            return;
        }
    }

    // ----- 롤백 처리 -----
    @RabbitListener(queues = "coupon4.rollback.queue",
            containerFactory = "rabbitListenerContainerFactory")
    @Transactional
    public void rollback(Long orderId) {
        log.error("🔥🔥 ROLLBACK 메시지 받음 orderId={}", orderId);
        List<Coupon> usedCoupons = couponRepo.findAllByUserOrderId(orderId);

        for (Coupon c : usedCoupons) {

            c.setStatus(CouponStatus.UNUSED);
            c.setUsedAt(null);
            c.setUserOrderId(null);

            couponRepo.save(c);
        }
    }

}