package com.nhnacademy._vidiacouponservice.domain.dto.request;

import com.nhnacademy._vidiacouponservice.domain.dto.response.OrderBookResponse;

import java.util.List;

public record OrderCouponRequest(
        List<OrderBookResponse> orderBookResponses
) {}
