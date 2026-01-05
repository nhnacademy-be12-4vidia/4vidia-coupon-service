package com.nhnacademy._vidiacouponservice.service;

import com.nhnacademy._vidiacouponservice.domain.*;
import com.nhnacademy._vidiacouponservice.domain.common.*;
import com.nhnacademy._vidiacouponservice.domain.dto.request.*;
import com.nhnacademy._vidiacouponservice.domain.dto.response.*;
import com.nhnacademy._vidiacouponservice.repository.UserCouponRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MyCouponServiceTest {

    @InjectMocks
    MyCouponService service;

    @Mock
    UserCouponRepository repo;

    private UserCoupon userCoupon(CouponStatus status, LocalDateTime expireAt) {
        CouponPolicy p = new CouponPolicy();
        p.setPolicyName("TEST");
        p.setDiscountType(DiscountType.PRICE);
        p.setDiscountValue(1000);
        p.setDiscountTargetType(DiscountTargetType.ALL);

        Coupon c = new Coupon();
        c.setCouponId(1L);
        c.setCouponPolicy(p);
        c.setStatus(status);
        c.setExpireAt(expireAt);
        c.setIssuedAt(LocalDateTime.now());

        return new UserCoupon(1L, c);
    }

    @Test
    @DisplayName("내 쿠폰 페이지 조회 (ALL)")
    void getMyCouponsPage_all() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<UserCoupon> page = new PageImpl<>(
                List.of(userCoupon(CouponStatus.UNUSED, LocalDateTime.now().plusDays(1)))
        );

        when(repo.findAllByIdUserId(1L, pageable)).thenReturn(page);
        when(repo.countByIdUserIdAndCoupon_Status(1L, CouponStatus.UNUSED)).thenReturn(1L);
        when(repo.countByIdUserIdAndCoupon_StatusAndCoupon_ExpireAtBefore(
                eq(1L), eq(CouponStatus.UNUSED), any(LocalDateTime.class))
        ).thenReturn(0L);

        MyCouponPageResponse res = service.getMyCouponsPage(1L, "ALL", pageable);

        assertThat(res.page().content()).hasSize(1);
        assertThat(res.totalCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("주문 화면 쿠폰 검증 - 사용 가능")
    void validate_orderCoupon_success() {
        UserCoupon uc = userCoupon(CouponStatus.UNUSED, LocalDateTime.now().plusDays(1));

        when(repo.findAllByIdUserId(1L)).thenReturn(List.of(uc));

        CouponValidateRequest req = new CouponValidateRequest(
                List.of(new CouponValidateItem(1L, "800", 10000, 1))
        );

        List<OrderPageCouponResponse> res = service.getOrderCoupons(1L, req);

        assertThat(res.get(0).available()).isTrue();
        assertThat(res.get(0).discountPrice()).isEqualTo(1000);
    }

    @Test
    @DisplayName("주문 화면 쿠폰 검증 - 이미 사용됨")
    void validate_usedCoupon() {
        UserCoupon uc = userCoupon(CouponStatus.USED, LocalDateTime.now().plusDays(1));

        when(repo.findAllByIdUserId(1L)).thenReturn(List.of(uc));

        CouponValidateRequest req = new CouponValidateRequest(
                List.of(new CouponValidateItem(1L, "800", 10000, 1))
        );

        OrderPageCouponResponse res =
                service.getOrderCoupons(1L, req).get(0);

        assertThat(res.available()).isFalse();
        assertThat(res.reason()).contains("이미 사용");
    }
}
