package com.nhnacademy._vidiacouponservice.domain.dto;

import com.nhnacademy._vidiacouponservice.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final int code;
    private final String message;
    private final Object details;

    public static ErrorResponse of(ErrorCode code, Object details) {
        return new ErrorResponse(code.getCode(), code.getMessage(), details);
    }
}
