package com.nhnacademy._vidiacouponservice.controller;

import com.nhnacademy._vidiacouponservice.docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(CategoryController.class)
class CategoryControllerTest extends RestDocsSupport {

    @Autowired
    MockMvc mockMvc;


    @Autowired
    CategoryController categoryController;

    @Override
    protected Object initController() {
        return categoryController;
    }

    @Test
    @DisplayName("카테고리 목록 조회")
    void getCategories_success() throws Exception {
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andDo(document("categories-get",
                        responseFields(
                                fieldWithPath("header.isSuccessful").description("성공 여부"),
                                fieldWithPath("header.resultCode").description("결과 코드"),
                                fieldWithPath("header.resultMessage").description("결과 메시지"),
                                fieldWithPath("header.errorCode").description("에러 코드"),
                                fieldWithPath("header.timestamp").description("응답 시간"),

                                fieldWithPath("data").description("카테고리 목록"),
                                fieldWithPath("data[].code").description("카테고리 코드"),
                                fieldWithPath("data[].name").description("카테고리 이름")
                        )
                ));

    }
}
