package com.nhnacademy._vidiacouponservice.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCodeProvider {
    HttpStatus getStatus();
    String getCode();
    String getMessage();
    String getName();
}
