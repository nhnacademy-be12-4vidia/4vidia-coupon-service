package com.nhnacademy._vidiacouponservice.service;

import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.dto.response.UseCouponResponse;
import com.nhnacademy._vidiacouponservice.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponRefundService {

    private final CouponRepository couponRepository;

    public UseCouponResponse getUseCouponDetail(Long orderId) {
        return couponRepository.findByUserOrderId(orderId)
                .map(coupon -> {
                    CouponPolicy p = coupon.getCouponPolicy();
                    return new UseCouponResponse(
                            p.getDiscountTargetType().name(),
                            p.getCategoryKdcId(),
                            p.getBookId(),
                            p.getMinOrderAmount()
                    );
                })
                .orElse(null);
    }
}
