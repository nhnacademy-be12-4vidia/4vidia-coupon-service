package com.nhnacademy._vidiacouponservice.domain.dto.response;

import com.nhnacademy._vidiacouponservice.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {

    private final String error;        // error name (enum name)
    private final String message;      // 상세 메시지
    private final int status;          // HTTP status
    private final LocalDateTime timestamp;

    public static ErrorResponse of(ErrorCode code, String detailMessage) {
        return ErrorResponse.builder()
                .error(code.name())
                .message(detailMessage)
                .status(code.getStatus().value())
                .timestamp(LocalDateTime.now())
                .build();
    }

}
