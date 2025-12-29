package com.nhnacademy._vidiacouponservice.service;

import com.nhnacademy._vidiacouponservice.domain.Coupon;
import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.common.CouponStatus;
import com.nhnacademy._vidiacouponservice.domain.common.DiscountTargetType;
import com.nhnacademy._vidiacouponservice.domain.common.DiscountType;
import com.nhnacademy._vidiacouponservice.domain.common.KdcCategory;
import com.nhnacademy._vidiacouponservice.domain.dto.request.CouponCalculationRequest;
import com.nhnacademy._vidiacouponservice.domain.dto.response.CouponCalculationResponse;
import com.nhnacademy._vidiacouponservice.exception.CouponAlreadyUsedException;
import com.nhnacademy._vidiacouponservice.exception.CouponExpireException;
import com.nhnacademy._vidiacouponservice.exception.CouponInvalidException;
import com.nhnacademy._vidiacouponservice.exception.CouponNotHoldException;
import com.nhnacademy._vidiacouponservice.repository.CouponRepository;
import com.nhnacademy._vidiacouponservice.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponCalculateService {

    private final UserCouponRepository userCouponRepo;
    private final CouponRepository couponRepo;

    public CouponCalculationResponse calculate(
            Long userId,
            CouponCalculationRequest req
    ) {

        Long couponId = req.couponId();

        // 1. 주문 원본 금액 계산
        int totalOriginalPrice = req.items().stream()
                .mapToInt(i -> i.price() * i.quantity())
                .sum();

        // 2. 쿠폰 소유 검증
        userCouponRepo.findByIdUserIdAndIdCouponId(userId, couponId)
                .orElseThrow(() -> new CouponNotHoldException(couponId));

        // 3. 쿠폰 조회
        Coupon coupon = couponRepo.findById(couponId)
                .orElseThrow(() -> new CouponNotHoldException(couponId));

        CouponPolicy policy = coupon.getCouponPolicy();

        // 4. 정책 활성화 여부
        if (!policy.getIsActivation()) {
            throw new CouponInvalidException(couponId, "비활성화된 쿠폰 정책");
        }

        // 5. 상태 검증
        validateCouponStatus(coupon);

        // 6. 주문 대상 검증
        validateTarget(policy, req);

        // 7. 최소 주문 금액
        if (totalOriginalPrice < policy.getMinOrderAmount()) {
            throw new CouponInvalidException(
                    couponId,
                    "최소 주문 금액 " + policy.getMinOrderAmount() + "원 이상 필요"
            );
        }

        // 8. 할인 계산
        int targetAmount = calculateTargetAmount(policy, req);

        int discountPrice = calculateDiscount(targetAmount, policy);
        return new CouponCalculationResponse(discountPrice);
    }

    /* ===================== 내부 메서드 ===================== */

    private void validateCouponStatus(Coupon coupon) {
        if (coupon.getStatus() != CouponStatus.UNUSED)
            throw new CouponAlreadyUsedException(coupon.getCouponId());

        if (coupon.getExpireAt().isBefore(LocalDateTime.now()))
            throw new CouponExpireException(coupon.getCouponId());
    }

    private void validateTarget(
            CouponPolicy policy,
            CouponCalculationRequest req
    ) {
        if (policy.getDiscountTargetType() == DiscountTargetType.CATEGORY) {

            String required = KdcCategory
                    .fromKdcId(policy.getCategoryKdcId())
                    .getCode();

            boolean matched = req.items().stream()
                    .anyMatch(i ->
                            KdcCategory.fromKdcId(i.categoryKdcId())
                                    .getCode()
                                    .equals(required)
                    );

            if (!matched)
                throw new CouponInvalidException(
                        policy.getPolicyId(),
                        "카테고리 불일치"
                );
        }

        if (policy.getDiscountTargetType() == DiscountTargetType.BOOK) {
            boolean matched = req.items().stream()
                    .anyMatch(i -> i.bookId().equals(policy.getBookId()));

            if (!matched)
                throw new CouponInvalidException(
                        policy.getPolicyId(),
                        "도서 불일치"
                );
        }
    }

    private int calculateTargetAmount(
            CouponPolicy policy,
            CouponCalculationRequest req
    ) {
        return switch (policy.getDiscountTargetType()) {

            case ALL -> req.items().stream()
                    .mapToInt(i -> i.price() * i.quantity())
                    .sum();

            case CATEGORY -> {
                String required = KdcCategory
                        .fromKdcId(policy.getCategoryKdcId())
                        .getCode();

                yield req.items().stream()
                        .filter(i ->
                                KdcCategory.fromKdcId(i.categoryKdcId())
                                        .getCode()
                                        .equals(required)
                        )
                        .mapToInt(i -> i.price() * i.quantity())
                        .sum();
            }

            case BOOK -> req.items().stream()
                    .filter(i -> i.bookId().equals(policy.getBookId()))
                    .mapToInt(i -> i.price() * i.quantity())
                    .sum();
        };
    }


    private int calculateDiscount(int targetAmount, CouponPolicy policy) {
        if (targetAmount <= 0) {
            return 0;
        }

        if (policy.getDiscountType() == DiscountType.PRICE) {
            return Math.min(policy.getDiscountValue(), targetAmount);
        }

        int discount = targetAmount * policy.getDiscountValue() / 100;

        if (policy.getMaxDiscountAmount() != null) {
            discount = Math.min(discount, policy.getMaxDiscountAmount());
        }

        return discount;
    }

}