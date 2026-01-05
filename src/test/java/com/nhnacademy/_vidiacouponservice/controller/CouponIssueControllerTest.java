package com.nhnacademy._vidiacouponservice.controller;

import com.nhnacademy._vidiacouponservice.docs.RestDocsSupport;
import com.nhnacademy._vidiacouponservice.service.CouponEventIssueService;
import com.nhnacademy._vidiacouponservice.service.CouponIssueService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CouponIssueController.class)
class CouponIssueControllerTest extends RestDocsSupport {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CouponIssueService issueService;

    @MockBean
    CouponEventIssueService eventIssueService;


    @Autowired
    CouponIssueController couponIssueController;

    @Override
    protected Object initController() {
        return couponIssueController;
    }

    @Test
    @DisplayName("선착순 쿠폰 발급 요청")
    void issue_coupon() throws Exception {

        mockMvc.perform(
                        post("/coupons/{policyId}/issue", 1L)
                                .header("X-User-Id", 10L)
                )
                .andExpect(status().isOk())
                .andDo(document("coupon-issue",
                        pathParameters(
                                parameterWithName("policyId").description("쿠폰 정책 ID")
                        ),
                        requestHeaders(
                                headerWithName("X-User-Id").description("유저 ID")
                        ),
                        responseFields(
                                fieldWithPath("header.isSuccessful").description("성공 여부"),
                                fieldWithPath("header.resultCode").description("결과 코드"),
                                fieldWithPath("header.resultMessage").description("결과 메시지"),
                                fieldWithPath("header.errorCode").description("에러 코드"),
                                fieldWithPath("header.timestamp").description("응답 시간")
                        )
                ));

        verify(issueService).issue(10L, 1L);
    }

    @Test
    @DisplayName("이벤트/웰컴/생일 쿠폰 발급 요청")
    void issue_event_coupon() throws Exception {

        mockMvc.perform(
                        post("/coupons/{policyId}/event-issue", 2L)
                                .header("X-User-Id", 20L)
                )
                .andExpect(status().isOk())
                .andDo(document("coupon-event-issue",
                        pathParameters(
                                parameterWithName("policyId").description("쿠폰 정책 ID")
                        ),
                        requestHeaders(
                                headerWithName("X-User-Id").description("유저 ID")
                        ),
                        responseFields(
                                fieldWithPath("header.isSuccessful").description("성공 여부"),
                                fieldWithPath("header.resultCode").description("결과 코드"),
                                fieldWithPath("header.resultMessage").description("결과 메시지"),
                                fieldWithPath("header.errorCode").description("에러 코드"),
                                fieldWithPath("header.timestamp").description("응답 시간")
                        )
                ));

        verify(eventIssueService).issueEventCoupon(20L, 2L);
    }
}
