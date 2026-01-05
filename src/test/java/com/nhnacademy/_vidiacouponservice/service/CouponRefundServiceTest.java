package com.nhnacademy._vidiacouponservice.service;

import com.nhnacademy._vidiacouponservice.domain.*;
import com.nhnacademy._vidiacouponservice.domain.common.*;
import com.nhnacademy._vidiacouponservice.domain.dto.response.UseCouponResponse;
import com.nhnacademy._vidiacouponservice.repository.CouponRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponRefundServiceTest {

    @InjectMocks
    CouponRefundService service;

    @Mock
    CouponRepository repo;

    @Test
    @DisplayName("환불 시 쿠폰 정보 반환")
    void refund_success() {
        CouponPolicy p = new CouponPolicy();
        p.setDiscountTargetType(DiscountTargetType.ALL);
        p.setMinOrderAmount(1000);

        Coupon c = new Coupon();
        c.setCouponPolicy(p);

        when(repo.findByUserOrderId(100L))
                .thenReturn(Optional.of(c));

        UseCouponResponse res = service.getUseCouponDetail(100L);

        assertThat(res.minOrderAmount()).isEqualTo(1000);
    }
}
