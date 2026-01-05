package com.nhnacademy._vidiacouponservice.controller;

import com.nhnacademy._vidiacouponservice.domain.dto.response.UseCouponResponse;
import com.nhnacademy._vidiacouponservice.service.CouponRefundService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CouponRefundController.class)
class CouponRefundControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CouponRefundService couponRefundService;

    @Test
    @DisplayName("쿠폰 사용 내역 환불 처리")
    void refundCoupon_success() throws Exception {
        given(couponRefundService.getUseCouponDetail(100L))
                .willReturn(new UseCouponResponse("ALL", null, null, 0));

        mockMvc.perform(post("/internal/coupons/refund")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "orderId": 100
                            }
                        """))
                .andExpect(status().isOk());
    }
}
