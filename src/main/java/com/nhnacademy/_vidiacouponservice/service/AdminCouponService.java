package com.nhnacademy._vidiacouponservice.service;

import com.nhnacademy._vidiacouponservice.domain.Coupon;
import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.common.CouponStatus;
import com.nhnacademy._vidiacouponservice.domain.dto.response.MyCouponResponse;
import com.nhnacademy._vidiacouponservice.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCouponService {

    private final UserCouponRepository userCouponRepo;

    public List<MyCouponResponse> getUserCoupons(Long userId) {
        return userCouponRepo.findAllByIdUserId(userId).stream()
                .map(uc -> {
                    Coupon c = normalizeExpire(uc.getCoupon());
                    CouponPolicy p = c.getCouponPolicy();

                    return new MyCouponResponse(
                            c.getCouponId(),
                            p.getPolicyName(),
                            p.getDiscountType().name(),
                            p.getDiscountValue(),
                            p.getMaxDiscountAmount(),
                            p.getDiscountTargetType().name(),
                            p.getCategoryKdcId(),
                            p.getBookId(),
                            c.getIssuedAt(),
                            c.getExpireAt(),
                            c.getStatus(),
                            c.getUserOrderId()
                    );
                })
                .toList();
    }

    private Coupon normalizeExpire(Coupon c) {
        if (c.getStatus() == CouponStatus.UNUSED &&
                c.getExpireAt().isBefore(LocalDateTime.now())) {

            c.setStatus(CouponStatus.EXPIRED);
            // 👉 여기서 save 해도 되고, 조회 전용이면 안 해도 됨
        }
        return c;
    }

}
