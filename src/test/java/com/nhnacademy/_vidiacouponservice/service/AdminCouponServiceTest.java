package com.nhnacademy._vidiacouponservice.service;

import com.nhnacademy._vidiacouponservice.domain.*;
import com.nhnacademy._vidiacouponservice.domain.common.*;
import com.nhnacademy._vidiacouponservice.repository.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminCouponServiceTest {

    @InjectMocks
    AdminCouponService service;

    @Mock
    UserCouponRepository userCouponRepo;

    @Mock
    CouponPolicyRepository policyRepo;

    @Test
    @DisplayName("관리자 유저 쿠폰 조회")
    void getUserCoupons() {
        CouponPolicy p = new CouponPolicy();
        p.setPolicyName("TEST");
        p.setDiscountType(DiscountType.PRICE);
        p.setDiscountValue(1000);
        p.setDiscountTargetType(DiscountTargetType.ALL);

        Coupon c = new Coupon();
        c.setCouponId(1L);
        c.setCouponPolicy(p);
        c.setIssuedAt(LocalDateTime.now());
        c.setExpireAt(LocalDateTime.now().plusDays(1));
        c.setStatus(CouponStatus.UNUSED);

        UserCoupon uc = new UserCoupon(1L, c);

        when(userCouponRepo.findAllByIdUserId(1L))
                .thenReturn(List.of(uc));

        assertThat(service.getUserCoupons(1L)).hasSize(1);
    }

    @Test
    @DisplayName("발급 가능 정책 필터링")
    void getIssuablePolicies() {
        CouponPolicy p1 = new CouponPolicy();
        p1.setPolicyId(1L);
        p1.setIsActivation(true);

        when(userCouponRepo.findPolicyIdsByUserId(1L))
                .thenReturn(List.of(1L));
        when(policyRepo.findAll()).thenReturn(List.of(p1));

        assertThat(service.getIssuablePolicies(1L)).isEmpty();
    }
}
