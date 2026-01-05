package com.nhnacademy._vidiacouponservice.controller;

import com.nhnacademy._vidiacouponservice.docs.RestDocsSupport;
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

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CouponPolicyController.class)
class CouponPolicyControllerTest extends RestDocsSupport {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CouponPolicyService couponPolicyService;


    @Autowired
    CouponPolicyController couponPolicyController;

    @Override
    protected Object initController() {
        return couponPolicyController;
    }

    @Test
    @DisplayName("활성화된 쿠폰 정책 전체 조회")
    void findAllActive_success() throws Exception {
        given(couponPolicyService.findAllActive())
                .willReturn(List.of());

        mockMvc.perform(get("/policies"))
                .andExpect(status().isOk())
                .andDo(document("coupon-policies-search",
                        queryParameters(
                                parameterWithName("page").description("페이지 번호").optional(),
                                parameterWithName("size").description("페이지 크기").optional()
                        ),
                        responseFields(
                                fieldWithPath("header.isSuccessful").description("성공 여부"),
                                fieldWithPath("header.resultCode").description("결과 코드"),
                                fieldWithPath("header.resultMessage").description("결과 메시지"),
                                fieldWithPath("header.errorCode").description("에러 코드"),
                                fieldWithPath("header.timestamp").description("응답 시간"),
                                fieldWithPath("data").description("검색 결과 페이지")
                        )
                ));

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
