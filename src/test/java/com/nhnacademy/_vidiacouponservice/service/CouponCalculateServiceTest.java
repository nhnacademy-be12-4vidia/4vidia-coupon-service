package com.nhnacademy._vidiacouponservice.service;

import com.nhnacademy._vidiacouponservice.domain.Coupon;
import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.common.*;
import com.nhnacademy._vidiacouponservice.domain.dto.request.CouponCalculationRequest;
import com.nhnacademy._vidiacouponservice.domain.dto.response.CouponCalculationResponse;
import com.nhnacademy._vidiacouponservice.exception.CouponAlreadyUsedException;
import com.nhnacademy._vidiacouponservice.exception.CouponInvalidException;
import com.nhnacademy._vidiacouponservice.exception.CouponNotHoldException;
import com.nhnacademy._vidiacouponservice.repository.CouponRepository;
import com.nhnacademy._vidiacouponservice.repository.UserCouponRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponCalculateServiceTest {

    @InjectMocks
    CouponCalculateService service;

    @Mock
    UserCouponRepository userCouponRepo;

    @Mock
    CouponRepository couponRepo;

    @Test
    @DisplayName("실패: 유저가 쿠폰을 안 갖고 있으면 CouponNotHoldException")
    void calculate_notHold() {
        Long userId = 1L;
        Long couponId = 10L;

        when(userCouponRepo.findByIdUserIdAndIdCouponId(userId, couponId))
                .thenReturn(Optional.empty());

        CouponCalculationRequest req = new CouponCalculationRequest(
                couponId,
                List.of(new CouponCalculationRequest.ItemInfo(1L, "800", 10000, 1))
        );

        assertThatThrownBy(() -> service.calculate(userId, req))
                .isInstanceOf(CouponNotHoldException.class);

        verify(couponRepo, never()).findById(anyLong());
    }

    @Test
    @DisplayName("실패: 쿠폰 status가 UNUSED가 아니면 CouponAlreadyUsedException")
    void calculate_alreadyUsed() {
        Long userId = 1L;
        Long couponId = 10L;

        when(userCouponRepo.findByIdUserIdAndIdCouponId(userId, couponId))
                .thenReturn(Optional.of(mock(com.nhnacademy._vidiacouponservice.domain.UserCoupon.class)));

        CouponPolicy policy = new CouponPolicy();
        policy.setIsActivation(true);

        Coupon coupon = new Coupon();
        coupon.setCouponId(couponId);
        coupon.setCouponPolicy(policy);
        coupon.setStatus(CouponStatus.USED);
        coupon.setExpireAt(LocalDateTime.now().plusDays(1));

        when(couponRepo.findById(couponId)).thenReturn(Optional.of(coupon));

        CouponCalculationRequest req = new CouponCalculationRequest(
                couponId,
                List.of(new CouponCalculationRequest.ItemInfo(1L, "800", 10000, 1))
        );

        assertThatThrownBy(() -> service.calculate(userId, req))
                .isInstanceOf(CouponAlreadyUsedException.class);
    }

    @Test
    @DisplayName("실패: CATEGORY 타겟인데 주문 아이템에 해당 카테고리 없으면 CouponInvalidException")
    void calculate_categoryMismatch() {
        Long userId = 1L;
        Long couponId = 10L;

        when(userCouponRepo.findByIdUserIdAndIdCouponId(userId, couponId))
                .thenReturn(Optional.of(mock(com.nhnacademy._vidiacouponservice.domain.UserCoupon.class)));

        CouponPolicy policy = new CouponPolicy();
        policy.setIsActivation(true);
        policy.setDiscountTargetType(DiscountTargetType.CATEGORY);
        policy.setCategoryKdcId("300"); // 사회과학 카테고리라고 치자
        policy.setDiscountType(DiscountType.PRICE);
        policy.setDiscountValue(1000);
        policy.setMinOrderAmount(0);

        Coupon coupon = new Coupon();
        coupon.setCouponId(couponId);
        coupon.setCouponPolicy(policy);
        coupon.setStatus(CouponStatus.UNUSED);
        coupon.setExpireAt(LocalDateTime.now().plusDays(1));

        when(couponRepo.findById(couponId)).thenReturn(Optional.of(coupon));

        // 주문은 "800" 문학 같은 거만 있음 → mismatch
        CouponCalculationRequest req = new CouponCalculationRequest(
                couponId,
                List.of(new CouponCalculationRequest.ItemInfo(1L, "800", 10000, 1))
        );

        assertThatThrownBy(() -> service.calculate(userId, req))
                .isInstanceOf(CouponInvalidException.class);
    }

    @Test
    @DisplayName("성공: ALL + PRICE(정액) 쿠폰이면 할인값만큼(단, 주문금액 초과불가)")
    void calculate_success_all_price() {
        Long userId = 1L;
        Long couponId = 10L;

        when(userCouponRepo.findByIdUserIdAndIdCouponId(userId, couponId))
                .thenReturn(Optional.of(mock(com.nhnacademy._vidiacouponservice.domain.UserCoupon.class)));

        CouponPolicy policy = new CouponPolicy();
        policy.setIsActivation(true);
        policy.setDiscountTargetType(DiscountTargetType.ALL);
        policy.setDiscountType(DiscountType.PRICE);
        policy.setDiscountValue(3000);
        policy.setMinOrderAmount(0);

        Coupon coupon = new Coupon();
        coupon.setCouponId(couponId);
        coupon.setCouponPolicy(policy);
        coupon.setStatus(CouponStatus.UNUSED);
        coupon.setExpireAt(LocalDateTime.now().plusDays(1));

        when(couponRepo.findById(couponId)).thenReturn(Optional.of(coupon));

        CouponCalculationRequest req = new CouponCalculationRequest(
                couponId,
                List.of(
                        new CouponCalculationRequest.ItemInfo(1L, "800", 10000, 1),
                        new CouponCalculationRequest.ItemInfo(2L, "800", 5000, 1)
                )
        );

        CouponCalculationResponse res = service.calculate(userId, req);

        assertThat(res.discountPrice()).isEqualTo(3000);
    }
}
