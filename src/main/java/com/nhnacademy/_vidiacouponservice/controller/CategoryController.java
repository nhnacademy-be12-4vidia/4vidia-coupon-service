package com.nhnacademy._vidiacouponservice.controller;

import com.nhnacademy._vidiacouponservice.domain.common.KdcCategory;
import com.nhnacademy._vidiacouponservice.domain.dto.response.KdcCategoryResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {


    @GetMapping
    public List<KdcCategoryResponse> getCategories() {
        return Arrays.stream(KdcCategory.values())
                .map(v -> new KdcCategoryResponse(v.getCode(), v.getDesc()))
                .toList();
    }
}
