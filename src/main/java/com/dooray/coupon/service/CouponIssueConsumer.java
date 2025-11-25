package com.dooray.coupon.service;

import com.dooray.coupon.config.RabbitMQConfig;
import com.dooray.coupon.domain.Coupon;
import com.dooray.coupon.domain.CouponPolicy;
import com.dooray.coupon.domain.UserCoupon;
import com.dooray.coupon.domain.common.CouponStatus;
import com.dooray.coupon.domain.common.ValidityType;
import com.dooray.coupon.domain.dto.CouponIssueMessage;
import com.dooray.coupon.repository.CouponPolicyRepository;
import com.dooray.coupon.repository.CouponRepository;
import com.dooray.coupon.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * MQ에서 메시지 하나씩 소비함
 * 얘가 진짜 쿠폰을 DB에 저장
 */


@Component
@RequiredArgsConstructor
public class CouponIssueConsumer {

    private final CouponPolicyRepository policyRepo;
    private final CouponRepository couponRepo;
    private final UserCouponRepository userCouponRepo;

    /**
     * 쿠폰생성+UserCoupon생성+정책 발급량 증가
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    @Transactional
    public void onMessage(CouponIssueMessage msg) {

        CouponPolicy policy = policyRepo.findById(msg.policyId())
                .orElseThrow(() -> new IllegalArgumentException("정책 없음"));

        Coupon coupon = new Coupon();
        coupon.setCouponPolicy(policy);
        coupon.setIssuedAt(LocalDateTime.now());
        coupon.setExpireAt(calcExpire(policy));
        coupon.setStatus(CouponStatus.UNUSED);

        couponRepo.save(coupon);

        userCouponRepo.save(new UserCoupon(msg.userId(), coupon));

        policy.setIssuedQuantity(policy.getIssuedQuantity() + 1);
    }

    private LocalDateTime calcExpire(CouponPolicy p) {
        if (p.getValidityType() == ValidityType.RELATIVE)
            return LocalDateTime.now().plusDays(p.getValid_days());
        return p.getEndDate();
    }
}
