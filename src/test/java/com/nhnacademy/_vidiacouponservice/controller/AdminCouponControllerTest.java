package com.nhnacademy._vidiacouponservice.controller;

import com.nhnacademy._vidiacouponservice.docs.RestDocsSupport;
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
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(AdminCouponController.class)
class AdminCouponControllerTest extends RestDocsSupport {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AdminCouponController adminCouponController;

    @MockBean
    AdminCouponService adminCouponService;
    @MockBean
    CouponIssueService couponIssueService;
    @MockBean
    CouponEventIssueService couponEventIssueService;

    @Override
    protected Object initController() {
        return adminCouponController;
    }

    @Test
    @DisplayName("관리자 - 특정 유저의 쿠폰 목록 조회")
    void getUserCoupons_success() throws Exception {
        given(adminCouponService.getUserCoupons(1L))
                .willReturn(List.of());

        mockMvc.perform(get("/admin/users/{userId}/coupons", 1L))
                .andExpect(status().isOk())
                .andDo(document("admin-user-coupons-get",
                        pathParameters(
                                parameterWithName("userId").description("유저 ID")
                        ),
                        responseFields(
                                fieldWithPath("header.isSuccessful").description("성공 여부"),
                                fieldWithPath("header.resultCode").description("결과 코드"),
                                fieldWithPath("header.resultMessage").description("결과 메시지"),
                                fieldWithPath("header.errorCode").description("에러 코드"),
                                fieldWithPath("header.timestamp").description("응답 시간"),
                                fieldWithPath("data").description("유저의 쿠폰 목록")
                        )
                ));

    }

    @Test
    @DisplayName("관리자 - 쿠폰 정책 기반 쿠폰 발급")
    void adminIssue_success() throws Exception {
        mockMvc.perform(post("/admin/users/{userId}/coupons/{policyId}/issue", 1L, 10L))
                .andExpect(status().isOk())
                .andDo(document("admin-coupon-issue",
                        pathParameters(
                                parameterWithName("userId").description("유저 ID"),
                                parameterWithName("policyId").description("쿠폰 정책 ID")
                        )
                ));

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
