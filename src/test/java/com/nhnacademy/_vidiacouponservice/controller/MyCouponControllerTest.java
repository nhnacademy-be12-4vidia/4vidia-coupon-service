package com.nhnacademy._vidiacouponservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy._vidiacouponservice.docs.RestDocsSupport;
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
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;




@WebMvcTest(MyCouponController.class)
class MyCouponControllerTest extends RestDocsSupport {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MyCouponService myCouponService;

    @Autowired
    ObjectMapper objectMapper;


    @Autowired
    MyCouponController myCouponController;

    @Override
    protected Object initController() {
        return myCouponController;
    }

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
                .andExpect(status().isOk())
                .andDo(document("my-coupons-get",
                        requestHeaders(
                                headerWithName("X-User-Id").description("유저 ID")
                        ),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 크기")
                        ),
                        responseFields(
                                fieldWithPath("header.isSuccessful").description("성공 여부"),
                                fieldWithPath("header.resultCode").description("결과 코드"),
                                fieldWithPath("header.resultMessage").description("결과 메시지"),
                                fieldWithPath("header.errorCode").description("에러 코드"),
                                fieldWithPath("header.timestamp").description("응답 시간"),
                                fieldWithPath("data.page").description("페이지 정보").optional(),
                                fieldWithPath("data.totalCount").description("총 쿠폰 수"),
                                fieldWithPath("data.expireSoonCount").description("만료 임박 쿠폰 수")
                        )
                ));
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
                .andExpect(status().isOk())
                .andDo(document("coupons-validate",
                        requestHeaders(
                                headerWithName("X-User-Id").description("유저 ID")
                        )
                ));
    }
}
