package com.nhnacademy._vidiacouponservice.controller;

import com.nhnacademy._vidiacouponservice.service.AdminCouponService;
import com.nhnacademy._vidiacouponservice.service.CouponEventIssueService;
import com.nhnacademy._vidiacouponservice.service.CouponIssueService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(AdminCouponController.class)
class AdminCouponControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AdminCouponService adminCouponService;
    @MockBean
    CouponIssueService couponIssueService;
    @MockBean
    CouponEventIssueService couponEventIssueService;

    @Test
    @DisplayName("관리자 - 특정 유저의 쿠폰 목록 조회")
    void getUserCoupons_success() throws Exception {
        given(adminCouponService.getUserCoupons(1L))
                .willReturn(List.of());

        mockMvc.perform(get("/admin/users/{userId}/coupons", 1L))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("관리자 - 쿠폰 정책 기반 쿠폰 발급")
    void adminIssue_success() throws Exception {
        mockMvc.perform(post("/admin/users/{userId}/coupons/{policyId}/issue", 1L, 10L))
                .andExpect(status().isOk());

        verify(couponIssueService).issue(1L, 10L);
    }

    @Test
    @DisplayName("관리자 - 이벤트 쿠폰 발급")
    void adminEventIssue_success() throws Exception {
        mockMvc.perform(post("/admin/users/{userId}/coupons/{policyId}/issue-event", 1L, 10L))
                .andExpect(status().isOk());

        verify(couponEventIssueService).issueEventCoupon(1L, 10L);
    }

    @Test
    @DisplayName("관리자 - 발급 가능한 쿠폰 정책 목록 조회")
    void getIssuablePolicies_success() throws Exception {
        given(adminCouponService.getIssuablePolicies(1L))
                .willReturn(List.of());

        mockMvc.perform(get("/admin/users/{userId}/coupons/issuable-policies", 1L))
                .andExpect(status().isOk());
    }
}
