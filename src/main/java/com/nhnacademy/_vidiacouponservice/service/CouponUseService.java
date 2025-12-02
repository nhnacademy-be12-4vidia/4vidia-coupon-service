package com.nhnacademy._vidiacouponservice.service;

import com.nhnacademy._vidiacouponservice.domain.Coupon;
import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.UserCoupon;
import com.nhnacademy._vidiacouponservice.domain.common.CouponStatus;
import com.nhnacademy._vidiacouponservice.domain.common.DiscountTargetType;
import com.nhnacademy._vidiacouponservice.domain.dto.request.CouponUseRequest;
import com.nhnacademy._vidiacouponservice.domain.dto.request.CouponValidateRequest;
import com.nhnacademy._vidiacouponservice.domain.dto.response.CouponValidateResponse;
import com.nhnacademy._vidiacouponservice.exception.CouponAlreadyUsed;
import com.nhnacademy._vidiacouponservice.exception.CouponNotHoldException;
import com.nhnacademy._vidiacouponservice.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponUseService {

    private final UserCouponRepository userCouponRepository;
    
    public CouponValidateResponse validateCoupon(Long couponId, CouponValidateRequest req) {
        UserCoupon userCoupon = userCouponRepository.findByIdUserIdAndIdCouponId(req.userId(), couponId).orElse(null);

        if (userCoupon == null) {
            return new CouponValidateResponse(
                    false, "해당 유저의 쿠폰이 아닙니다",
                    null,null,null,null,null,null,null,null
            );
        }

        Coupon coupon = userCoupon.getCoupon();
        CouponPolicy couponPolicy = coupon.getCouponPolicy();

        // 만료체크
        if(coupon.getExpireAt().isBefore(LocalDateTime.now())){
            return new CouponValidateResponse(
                    false, "쿠폰이 만료되었습니다.",
                    null,null,null,null,null,null,null,null
            );
        }

        // 이미사용됨
        if(coupon.getStatus().isUsed()){
            return new CouponValidateResponse(
                    false, "이미 사용된 쿠폰입니다.",
                    null,null,null,null,null,null,null,null
            );
        }

        // 최소 주문 금액 체크
        if(couponPolicy.getMinOrderAmount() != null &&
                req.orderAmount() < couponPolicy.getMinOrderAmount()){
            return new CouponValidateResponse(
                    false, "최소 주믄 금액 조건 불충족",
                    null,null,null,null,null,null,null,null
            );
        }

        // 카테고리쿠폰
        if(couponPolicy.getDiscountTargetType() == DiscountTargetType.CATEGORY){
            if(couponPolicy.getCategoryId() == null || !couponPolicy.getCategoryId().equals(req.categoryId())){
                return new CouponValidateResponse(
                        false, "해당 카테고리에서 사용할 수 없는 쿠폰입니다.",
                        null,null,null,null,null,null,null,null
                );
            }
        }

        // 도서쿠폰
        if(couponPolicy.getDiscountTargetType() == DiscountTargetType.BOOK){
            if(couponPolicy.getBookId() == null || !couponPolicy.getBookId().equals(req.bookId())){
                return new CouponValidateResponse(
                        false, "해당 도서에서 사용할 수 없는 쿠폰입니다.",
                        null,null,null,null,null,null,null,null
                );
            }
        }

        // 모든 검증 통과 → 할인정보 반환
        return new CouponValidateResponse(
                true,
                "사용 가능",
                couponPolicy.getDiscountType().name(),
                couponPolicy.getDiscountValue(),
                couponPolicy.getMaxDiscountAmount(),
                couponPolicy.getDiscountTargetType().name(),
                couponPolicy.getCategoryId(),
                couponPolicy.getBookId(),
                couponPolicy.getMinOrderAmount(),
                coupon.getExpireAt().toString()
        );
    }

    @Transactional
    public void useCoupon(Long couponId, CouponUseRequest req) {
        UserCoupon userCoupon = userCouponRepository.findByIdUserIdAndIdCouponId(req.userId(), couponId)
                .orElseThrow(()->new CouponNotHoldException(couponId));

        Coupon coupon = userCoupon.getCoupon();

        if (coupon.getStatus().isUsed()){
            throw new CouponAlreadyUsed(couponId);
        }

        coupon.setStatus(CouponStatus.USED);
        coupon.setUsedAt(LocalDateTime.now());
        coupon.setUserOrderId(req.orderId());

    }

}
