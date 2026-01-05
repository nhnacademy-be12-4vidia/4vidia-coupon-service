package com.nhnacademy._vidiacouponservice.controller;

import com.nhnacademy._vidiacouponservice.service.WelcomeCouponService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WelcomeCouponController.class)
class WelcomeCouponControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WelcomeCouponService welcomeCouponService;


    @Test
    @DisplayName("웰컴 쿠폰 자동 발급 요청")
    void give_welcome_coupon() throws Exception {

        mockMvc.perform(
                post("/internal/coupons/welcome")
                        .header("X-User-Id", 1L)
        )
        .andExpect(status().isOk());

        verify(welcomeCouponService).giveWelcomeCoupon(1L);
    }
}
