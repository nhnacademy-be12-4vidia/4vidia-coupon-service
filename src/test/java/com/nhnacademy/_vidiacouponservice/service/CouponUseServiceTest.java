package com.nhnacademy._vidiacouponservice.service;

import com.nhnacademy._vidiacouponservice.domain.Coupon;
import com.nhnacademy._vidiacouponservice.domain.common.CouponStatus;
import com.nhnacademy._vidiacouponservice.domain.dto.request.CouponUseRequest;
import com.nhnacademy._vidiacouponservice.exception.CouponAlreadyUsedException;
import com.nhnacademy._vidiacouponservice.exception.CouponExpireException;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponUseServiceTest {

    @InjectMocks
    CouponUseService service;

    @Mock
    UserCouponRepository userCouponRepo;

    @Mock
    CouponRepository couponRepo;

    @Test
    @DisplayName("성공: 보유 검증 통과 + 조건부 update 성공(updated=1)")
    void useCoupons_success() {
        Long userId = 1L;
        Long couponId = 10L;
        Long orderId = 100L;

        // 보유 검증 통과
        when(userCouponRepo.findByIdUserIdAndIdCouponId(userId, couponId))
                .thenReturn(Optional.of(mock(com.nhnacademy._vidiacouponservice.domain.UserCoupon.class)));

        // 조건부 UPDATE 성공
        when(couponRepo.useCouponIfUnusedAndNotExpired(
                eq(couponId),
                eq(orderId),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                eq(CouponStatus.UNUSED),
                eq(CouponStatus.USED)
        )).thenReturn(1);

        assertThatCode(() -> service.useCoupons(userId, new CouponUseRequest(couponId, orderId)))
                .doesNotThrowAnyException();

        // updated=1이면 findById() 조회 안 함
        verify(couponRepo, never()).findById(anyLong());
    }

    @Test
    @DisplayName("실패: 유저가 쿠폰을 보유하지 않으면 CouponNotHoldException")
    void useCoupons_notHold() {
        Long userId = 1L;
        Long couponId = 10L;
        Long orderId = 100L;

        when(userCouponRepo.findByIdUserIdAndIdCouponId(userId, couponId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.useCoupons(userId, new CouponUseRequest(couponId, orderId)))
                .isInstanceOf(CouponNotHoldException.class);

        verify(couponRepo, never()).useCouponIfUnusedAndNotExpired(any(), any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("실패: 조건부 update 실패(updated=0) + 쿠폰 상태가 UNUSED가 아니면 CouponAlreadyUsedException")
    void useCoupons_alreadyUsed() {
        Long userId = 1L;
        Long couponId = 10L;
        Long orderId = 100L;

        when(userCouponRepo.findByIdUserIdAndIdCouponId(userId, couponId))
                .thenReturn(Optional.of(mock(com.nhnacademy._vidiacouponservice.domain.UserCoupon.class)));

        when(couponRepo.useCouponIfUnusedAndNotExpired(
                eq(couponId), eq(orderId),
                any(LocalDateTime.class), any(LocalDateTime.class),
                eq(CouponStatus.UNUSED), eq(CouponStatus.USED)
        )).thenReturn(0);

        Coupon coupon = new Coupon();
        coupon.setCouponId(couponId);
        coupon.setStatus(CouponStatus.USED); // 이미 사용됨
        coupon.setExpireAt(LocalDateTime.now().plusDays(1));

        when(couponRepo.findById(couponId)).thenReturn(Optional.of(coupon));

        assertThatThrownBy(() -> service.useCoupons(userId, new CouponUseRequest(couponId, orderId)))
                .isInstanceOf(CouponAlreadyUsedException.class);

        // 너 코드상 USED인데 EXPIRED로 바꿔버림 (현재 로직 그대로 검증)
        assertThat(coupon.getStatus()).isEqualTo(CouponStatus.EXPIRED);
    }

    @Test
    @DisplayName("실패: 조건부 update 실패(updated=0) + 만료된 쿠폰이면 CouponExpireException")
    void useCoupons_expired() {
        Long userId = 1L;
        Long couponId = 10L;
        Long orderId = 100L;

        when(userCouponRepo.findByIdUserIdAndIdCouponId(userId, couponId))
                .thenReturn(Optional.of(mock(com.nhnacademy._vidiacouponservice.domain.UserCoupon.class)));

        when(couponRepo.useCouponIfUnusedAndNotExpired(
                eq(couponId), eq(orderId),
                any(LocalDateTime.class), any(LocalDateTime.class),
                eq(CouponStatus.UNUSED), eq(CouponStatus.USED)
        )).thenReturn(0);

        Coupon coupon = new Coupon();
        coupon.setCouponId(couponId);
        coupon.setStatus(CouponStatus.UNUSED);
        coupon.setExpireAt(LocalDateTime.now().minusSeconds(1)); // 만료

        when(couponRepo.findById(couponId)).thenReturn(Optional.of(coupon));

        assertThatThrownBy(() -> service.useCoupons(userId, new CouponUseRequest(couponId, orderId)))
                .isInstanceOf(CouponExpireException.class);
    }
}
