//package com.nhnacademy._vidiacouponservice.exception;
//
//import com.nhnacademy._vidiacouponservice.domain.Coupon;
//import com.nhnacademy._vidiacouponservice.domain.dto.response.ErrorResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.servlet.resource.NoResourceFoundException;
//
//@Slf4j
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    private ResponseEntity<ErrorResponse> build(ErrorCode code, Exception ex) {
//        return ResponseEntity
//                .status(code.getStatus())
//                .body(ErrorResponse.of(code, ex.getMessage()));
//    }
//
//    // ---- 정책 NotFound ----
//    @ExceptionHandler(PolicyNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handlePolicyNotFound(PolicyNotFoundException ex) {
//        return build(ErrorCode.POLICY_NOT_FOUND, ex);
//    }
//
//    // ---- 정책 비활성 ----
//    @ExceptionHandler(PolicyInactiveException.class)
//    public ResponseEntity<ErrorResponse> handleInactive(PolicyInactiveException ex) {
//        return build(ErrorCode.POLICY_INACTIVE, ex);
//    }
//
//    // ---- 재고 없음 (Redis key 없음) ----
//    @ExceptionHandler(PolicyStockMissingException.class)
//    public ResponseEntity<ErrorResponse> handleStockMissing(PolicyStockMissingException ex) {
//        return build(ErrorCode.POLICY_STOCK_MISSING, ex);
//    }
//
//    // ---- 매진 ----
//    @ExceptionHandler(PolicyOutOfStockException.class)
//    public ResponseEntity<ErrorResponse> handleOutOfStock(PolicyOutOfStockException ex) {
//        return build(ErrorCode.POLICY_OUT_OF_STOCK, ex);
//    }
//
//    // 쿠폰 이미 사용됨
//    @ExceptionHandler(CouponAlreadyUsedException.class)
//    public ResponseEntity<ErrorResponse> handleAlreadyUsed(CouponAlreadyUsedException ex) {
//        return build(ErrorCode.COUPON_ALREADY_USED, ex);
//    }
//
//    // 쿠폰이 없음
//    @ExceptionHandler(CouponNotHoldException.class)
//    public ResponseEntity<ErrorResponse> handleNotHold(CouponNotHoldException ex) {
//        return build(ErrorCode.COUPON_NOT_HOLD, ex);
//    }
//
//    // 쿠폰이 만료됨
//    @ExceptionHandler(CouponExpireException.class)
//    public ResponseEntity<ErrorResponse> handleExpiredCoupon(CouponExpireException ex) {
//        return build(ErrorCode.COUPON_EXPIRED, ex);
//    }
//
//    // 검증 실패
//    @ExceptionHandler(CouponInvalidException.class)
//    public ResponseEntity<ErrorResponse> handleCouponInvalid(CouponInvalidException ex) {
//        return build(ErrorCode.COUPON_INVALID, ex);
//    }
//
//    // 관리자
//    @ExceptionHandler(PolicyNotAdminIssuableException.class)
//    public ResponseEntity<ErrorResponse> handleNotAdminIssuable(PolicyNotAdminIssuableException ex) {
//        return build(ErrorCode.POLICY_INVALID, ex);
//    }
//
//    // 중복발급요청중임
//    @ExceptionHandler(DuplicateIssueRequestException.class)
//    public ResponseEntity<ErrorResponse> handleDuplicateIssue(DuplicateIssueRequestException ex) {
//        return build(ErrorCode.POLICY_INVALID, ex);
//    }
//
//
//
//
//    // ---- 기본 IllegalArgument ----
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
//        return build(ErrorCode.POLICY_INVALID, ex);
//    }
//
//    // ---- Fallback ----
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
//        log.error("UNEXPECTED ERROR", ex);
//        return build(ErrorCode.INTERNAL_SERVER_ERROR, ex);
//    }
//
//    // ---- NoResource ----
//    @ExceptionHandler(NoResourceFoundException.class)
//    public ResponseEntity<ErrorResponse> handleNoResource(NoResourceFoundException e) {
//
//        if (e.getResourcePath() != null &&
//                e.getResourcePath().startsWith("/.well-known/appspecific/")) {
//            return ResponseEntity.notFound().build();
//        }
//
//        return build(ErrorCode.POLICY_NOT_FOUND, e);
//    }
//}
