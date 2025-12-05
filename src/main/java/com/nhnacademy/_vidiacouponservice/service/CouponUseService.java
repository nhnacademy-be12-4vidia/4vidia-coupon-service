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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponUseService {

    private final UserCouponRepository userCouponRepo;
    private final CouponRepository couponRepo;

    public void useCoupon(Long userId, CouponUseRequest req) {

        // 1) 유저의 쿠폰인지 먼저 확인
        userCouponRepo.findByIdUserIdAndIdCouponId(userId, req.couponId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저의 쿠폰이 아닙니다."));

        // 2) 쿠폰 본체 조회
        Coupon coupon = couponRepo.findById(req.couponId())
                .orElseThrow(() -> new CouponNotHoldException(null));

        // 3) 이미 사용되거나 만료됨
        if (coupon.getStatus() != CouponStatus.UNUSED) {
            throw new CouponAlreadyUsedException(null);
        }

        // 4) 쿠폰 만료됨
        if (coupon.getExpireAt().isBefore(LocalDateTime.now())) {
            coupon.setStatus(CouponStatus.EXPIRED);
            couponRepo.save(coupon);
            throw new CouponExpireException(null);
        }

        coupon.setUsedAt(LocalDateTime.now());
        coupon.setUserOrderId(req.orderId());
        coupon.setStatus(CouponStatus.USED);

        couponRepo.save(coupon);
    }

}
