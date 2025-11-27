package com.nhnacademy._vidiacouponservice.service;


import com.nhnacademy._vidiacouponservice.config.RabbitMQConfig;
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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * MQ에서 메시지 하나씩 소비함
 * 얘가 진짜 쿠폰을 DB에 저장
 */
/**
 * 쿠폰 발급 이벤트를 처리하는 Consumer.
 * topic: coupon.issue.# 에 묶인 큐에서 메시지를 소비한다.
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
    @RabbitListener(queues = RabbitMQConfig.ISSUE_QUEUE)
    @Transactional
    public void onMessage(CouponIssueMessage msg) {

        CouponPolicy policy = policyRepo.findById(msg.policyId())
                .orElseThrow(() -> new PolicyInactiveException(msg.policyId()));

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
            return LocalDateTime.now().plusDays(p.getValidDays());
        return p.getEndDate();
    }
}