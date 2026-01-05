package com.nhnacademy._vidiacouponservice.controller;

import com.nhnacademy._vidiacouponservice.service.BirthdayCouponService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BirthdayCouponController.class)
class BirthdayCouponControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BirthdayCouponService birthdayCouponService;

    @Test
    @DisplayName("생일 쿠폰 지급 요청")
    void give_birthday_coupon() throws Exception {

        mockMvc.perform(
                post("/internal/policies/birthday")
                        .header("X-User-Id", 5L)
        )
        .andExpect(status().isOk());

        verify(birthdayCouponService).giveBirthdayCoupon(5L);
    }
}
