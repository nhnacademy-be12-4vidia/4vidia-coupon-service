package com.nhnacademy._vidiacouponservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy._vidiacouponservice.domain.dto.request.OrderCouponRequest;
import com.nhnacademy._vidiacouponservice.domain.dto.response.MyCouponPageResponse;
import com.nhnacademy._vidiacouponservice.service.MyCouponService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MyCouponController.class)
class MyCouponControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MyCouponService myCouponService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("내 쿠폰 페이지 조회")
    void get_my_coupons() throws Exception {

        when(myCouponService.getMyCouponsPage(any(), any(), any()))
                .thenReturn(mock(MyCouponPageResponse.class));

        mockMvc.perform(
                get("/coupons/me")
                        .header("X-User-Id", 1L)
                        .param("page", "0")
                        .param("size", "10")
        )
        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("주문 화면 쿠폰 검증")
    void validate_coupons() throws Exception {

        when(myCouponService.getOrderCoupons(any(), any(OrderCouponRequest.class)))
                .thenReturn(List.of());

        mockMvc.perform(
                post("/coupons/validate")
                        .header("X-User-Id", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new OrderCouponRequest(List.of())
                        ))
        )
        .andExpect(status().isOk());
    }
}
