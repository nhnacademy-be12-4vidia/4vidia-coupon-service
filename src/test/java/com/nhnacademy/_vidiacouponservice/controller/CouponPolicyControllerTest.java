package com.nhnacademy._vidiacouponservice.controller;

import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.service.CouponPolicyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.data.domain.Page;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CouponPolicyController.class)
class CouponPolicyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CouponPolicyService couponPolicyService;

    @Test
    @DisplayName("활성화된 쿠폰 정책 전체 조회")
    void findAllActive_success() throws Exception {
        given(couponPolicyService.findAllActive())
                .willReturn(List.of());

        mockMvc.perform(get("/policies"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("전체 쿠폰 정책 조회 (관리자)")
    void findAll_success() throws Exception {
        given(couponPolicyService.findAll())
                .willReturn(List.of());

        mockMvc.perform(get("/policies/all"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("쿠폰 정책 단건 조회")
    void find_success() throws Exception {
        given(couponPolicyService.find(1L))
                .willReturn(new CouponPolicy());

        mockMvc.perform(get("/policies/{policyId}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("쿠폰 정책 활성화 상태 토글")
    void toggleActivation_success() throws Exception {
        mockMvc.perform(patch("/policies/{policyId}/toggle", 1L))
                .andExpect(status().isOk());

        verify(couponPolicyService).toggleActivation(1L);
    }

    @Test
    @DisplayName("쿠폰 정책 검색")
    void search_success() throws Exception {
        given(couponPolicyService.search(any(), any(), any(), any()))
                .willReturn(Page.empty());

        mockMvc.perform(get("/policies/search"))
                .andExpect(status().isOk());
    }
}
