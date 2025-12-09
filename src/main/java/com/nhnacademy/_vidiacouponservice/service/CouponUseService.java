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
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponUseService {

    private final UserCouponRepository userCouponRepo;
    private final CouponRepository couponRepo;

    @Transactional
    public void useCoupons(Long userId, CouponUseRequest req) {

        log.error("🔥 CouponUseService.useCoupons START orderId={}, coupons={}",
                req.orderId(), req.couponIds());

        Long orderId = req.orderId();

        for (Long couponId : req.couponIds()) {

            // 1) 유저가 해당 쿠폰을 소유했는지 체크
            userCouponRepo.findByIdUserIdAndIdCouponId(userId, couponId)
                    .orElseThrow(() -> new CouponNotHoldException(couponId));

            // 2) 쿠폰 엔티티 조회
            Coupon coupon = couponRepo.findById(couponId)
                    .orElseThrow(() -> new CouponNotHoldException(couponId));

            // 3) 이미 사용된 쿠폰인지 체크
            if (coupon.getStatus() != CouponStatus.UNUSED) {
                throw new CouponAlreadyUsedException(couponId);
            }

            // 4) 만료 여부 체크
            if (coupon.getExpireAt().isBefore(LocalDateTime.now())) {
                coupon.setStatus(CouponStatus.EXPIRED);
                couponRepo.save(coupon);
                throw new CouponExpireException(couponId);
            }

            log.error("💾 쿠폰 사용 처리 → couponId={}, orderId={}", couponId, orderId);


            // 5) 쿠폰 사용 처리
            coupon.setUsedAt(LocalDateTime.now());
            coupon.setUserOrderId(orderId);
            coupon.setStatus(CouponStatus.USED);

            couponRepo.save(coupon);
        }
    }

}
