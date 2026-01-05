package com.nhnacademy._vidiacouponservice.controller;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CouponIssueController.class)
class CouponIssueControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CouponIssueService issueService;

    @MockBean
    CouponEventIssueService eventIssueService;

    @Test
    @DisplayName("선착순 쿠폰 발급 요청")
    void issue_coupon() throws Exception {

        mockMvc.perform(
                post("/coupons/{policyId}/issue", 1L)
                        .header("X-User-Id", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk());

        verify(issueService).issue(10L, 1L);
    }

    @Test
    @DisplayName("이벤트/웰컴/생일 쿠폰 발급 요청")
    void issue_event_coupon() throws Exception {

        mockMvc.perform(
                post("/coupons/{policyId}/event-issue", 2L)
                        .header("X-User-Id", 20L)
                        .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk());

        verify(eventIssueService).issueEventCoupon(20L, 2L);
    }
}
