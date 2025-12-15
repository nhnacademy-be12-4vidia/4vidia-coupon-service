package com.nhnacademy._vidiacouponservice.service;

import com.nhnacademy._vidiacouponservice.domain.Coupon;
import com.nhnacademy._vidiacouponservice.domain.common.CouponStatus;
import com.nhnacademy._vidiacouponservice.domain.dto.request.CouponUseRequest;
import com.nhnacademy._vidiacouponservice.exception.CouponAlreadyUsedException;
import com.nhnacademy._vidiacouponservice.exception.CouponExpireException;
import com.nhnacademy._vidiacouponservice.exception.CouponNotHoldException;
import com.nhnacademy._vidiacouponservice.repository.CouponRepository;
import com.nhnacademy._vidiacouponservice.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponUseService {

    private final UserCouponRepository userCouponRepo;
    private final CouponRepository couponRepo;

    @Transactional
    public void useCoupons(Long userId, CouponUseRequest req) {

        log.info("🔥 CouponUseService.useCoupons START orderId={}, couponId={}",
                req.orderId(), req.couponId());

        Long couponId = req.couponId();
        Long orderId = req.orderId();

        // 1. 소유 검증
        userCouponRepo.findByIdUserIdAndIdCouponId(userId, couponId)
                .orElseThrow(() -> new CouponNotHoldException(couponId));

        // 2. 조건부 UPDATE (핵심)
        int updated = couponRepo.useCouponIfUnusedAndNotExpired(
                couponId, orderId, LocalDateTime.now(), LocalDateTime.now()
        );

        if (updated == 1) {
            return; // 성공
        }

        // 2️⃣ 실패 시 원인 판별

        // 없는쿠폰 사용
        Coupon coupon = couponRepo.findById(couponId)
                .orElseThrow(() -> new CouponNotHoldException(couponId));

        //이미 사용됨
        if (coupon.getStatus() != CouponStatus.UNUSED) {
            throw new CouponAlreadyUsedException(couponId);
        }

        // 만료됨
        if (coupon.getExpireAt().isBefore(LocalDateTime.now())) {
            throw new CouponExpireException(couponId);
        }

        log.info("💾 쿠폰 사용 완료 → couponId={}, orderId={}", couponId, orderId);
    }



}
