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
import java.util.Map;
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

    // 재고에 제한이 있는
    @RabbitListener(
            queues = "coupon4.issue.queue",
            containerFactory = "rabbitListenerContainerFactory"
    )
    @Transactional
    public void consume(CouponIssueMessage msg) {

        // 정책 조회
        CouponPolicy policy = policyRepo.findById(msg.policyId()).orElse(null);
        if (policy == null) {
            log.warn("⚠️ 정책 없음 → 메시지 무시 policyId={}", msg.policyId());
            return;
        }

        // 이미 발급된 유저인지 체크 (멱등성)
        boolean alreadyIssued =
                userCouponRepo.existsByIdUserIdAndPolicyId(msg.userId(), msg.policyId());

        if (alreadyIssued) {
            log.warn("⚠️ 중복 발급 메시지 무시 user={}, policy={}",
                    msg.userId(), msg.policyId());
            return;
        }

        // 쿠폰 생성
        Coupon coupon = Coupon.issue(policy, msg.issuedAt(), msg.expireAt());
        couponRepo.save(coupon);

        // user_coupon 생성
        userCouponRepo.save(new UserCoupon(msg.userId(), coupon));

        // issued_quantity 증가
        policy.increaseIssuedQuantity();
        policyRepo.save(policy);

        // Redis issued 증가
        redis.opsForHash()
                .increment(RedisKeys.policyHash(msg.policyId()), "issued", 1);

        log.info("✅ 쿠폰 발급 완료 user={}, policy={}, couponId={}",
                msg.userId(), msg.policyId(), coupon.getCouponId());
    }


    // 웰컴/생일/이벤트 (재고 무제한)
    @RabbitListener(
            queues = "coupon4.event.queue",
            containerFactory = "rabbitListenerContainerFactory"
    )
    @Transactional
    public void consumeEvent(CouponIssueMessage msg) {

        CouponPolicy policy = policyRepo.findById(msg.policyId()).orElse(null);
        if (policy == null) {
            log.warn("⚠️ 정책 없음 → 이벤트 쿠폰 무시 policyId={}", msg.policyId());
            return;
        }

        boolean alreadyIssued =
                userCouponRepo.existsByIdUserIdAndPolicyId(msg.userId(), msg.policyId());

        if (alreadyIssued) {
            log.warn("⚠️ 이벤트 중복 발급 무시 user={}, policy={}",
                    msg.userId(), msg.policyId());
            return;
        }

        Coupon coupon = Coupon.issue(policy, msg.issuedAt(), msg.expireAt());
        couponRepo.save(coupon);

        userCouponRepo.save(new UserCoupon(msg.userId(), coupon));

        log.info("✅ 이벤트 쿠폰 발급 완료 user={}, policy={}",
                msg.userId(), msg.policyId());
    }


    /**
     * ⚠️ 정상 결제 흐름에서는 사용하지 않음
     * 결제 성공 이후 장애 발생 시 보상 트랜잭션 용도
     */

    // ----- 롤백 처리 -----
    @RabbitListener(
            queues = "coupon4.rollback.queue",
            containerFactory = "rabbitListenerContainerFactory"
    )
    @Transactional
    public void rollback(String orderIdStr) {

        Long orderId = Long.valueOf(orderIdStr);

        log.warn("🔄 쿠폰 롤백 이벤트 수신 orderId={}", orderId);

        int rolledBackCount = couponRepo.rollbackCouponsByOrderId(
                orderId,
                CouponStatus.USED,
                CouponStatus.UNUSED
        );


        if (rolledBackCount > 0) {
            log.info("✅ 쿠폰 롤백 완료 orderId={}, count={}", orderId, rolledBackCount);
        } else {
            log.info("ℹ️ 롤백 대상 없음 (이미 처리됨 또는 무시) orderId={}", orderId);
        }
    }


}