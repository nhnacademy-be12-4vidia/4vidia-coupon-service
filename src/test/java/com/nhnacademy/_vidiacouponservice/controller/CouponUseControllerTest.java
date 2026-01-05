package com.nhnacademy._vidiacouponservice.controller;

import com.nhnacademy._vidiacouponservice.domain.dto.request.CouponUseRequest;
import com.nhnacademy._vidiacouponservice.service.CouponUseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CouponUseController.class)
class CouponUseControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CouponUseService couponUseService;

    @Test
    @DisplayName("쿠폰 사용 처리")
    void useCoupon_success() throws Exception {
        mockMvc.perform(post("/internal/coupons/use")
                        .header("X-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "couponId": 10,
                              "orderId": 100
                            }
                        """))
                .andExpect(status().isOk());

        verify(couponUseService)
                .useCoupons(eq(1L), any(CouponUseRequest.class));
    }
}
