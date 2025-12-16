package com.nhnacademy._vidiacouponservice.domain.dto.response;

public record OrderBookResponse(
        Long bookId,
        String categoryKdc,
        Integer quantity,
        Integer salePrice
) {}
