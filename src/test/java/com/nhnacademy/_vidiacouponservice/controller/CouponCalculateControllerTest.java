package com.nhnacademy._vidiacouponservice.controller;


import com.nhnacademy._vidiacouponservice.domain.dto.response.CouponCalculationResponse;
import com.nhnacademy._vidiacouponservice.service.CouponCalculateService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CouponCalculateController.class)
class CouponCalculateControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CouponCalculateService couponCalculateService;

    @Test
    @DisplayName("쿠폰 할인 금액 계산")
    void calculateCoupon_success() throws Exception {
        given(couponCalculateService.calculate(anyLong(), any()))
                .willReturn(new CouponCalculationResponse(3000));

        mockMvc.perform(post("/internal/coupons/calculate")
                        .header("X-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "couponId": 1,
                              "items": []
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.discountPrice").value(3000));
    }
}
