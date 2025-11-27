package com.nhnacademy._vidiacouponservice.exception;

import com.nhnacademy._vidiacouponservice.domain.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // ---- 정책 없음 ----
    @ExceptionHandler(PolicyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlePolicyNotFound(PolicyNotFoundException ex) {
        return ErrorResponse.of(ErrorCode.POLICY_NOT_FOUND, ex.getMessage());
    }

    // ---- 정책 비활성 ----
    @ExceptionHandler(PolicyInactiveException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInactive(PolicyInactiveException ex) {
        return ErrorResponse.of(ErrorCode.POLICY_INACTIVE, ex.getMessage());
    }

    // ---- 재고 없음 ----
    @ExceptionHandler(PolicyStockMissingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleStockMissing(PolicyStockMissingException ex) {
        return ErrorResponse.of(ErrorCode.POLICY_INVALID, ex.getMessage());
    }

    // ---- 매진 ----
    @ExceptionHandler(PolicyOutOfStockException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleOutOfStock(PolicyOutOfStockException ex) {
        return ErrorResponse.of(ErrorCode.POLICY_OUT_OF_STOCK, ex.getMessage());
    }

    // ---- 기본 IllegalArgument ----
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgument(IllegalArgumentException ex) {
        return ErrorResponse.of(ErrorCode.POLICY_INVALID, ex.getMessage());
    }

    // ---- 마지막 fallback ----
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception ex) {
        log.error("UNEXPECTED ERROR", ex);
        return ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Void> handleNoResource(NoResourceFoundException e) {
        // .well-known/appspecific/ 인 경우는 그냥 로그도 안 찍고 넘기기
        if (e.getResourcePath() != null &&
                e.getResourcePath().startsWith("/.well-known/appspecific/")) {
            return ResponseEntity.notFound().build();
        }

        // 나머지는 기존 로직 태우기
        log.error("UNEXPECTED ERROR", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
