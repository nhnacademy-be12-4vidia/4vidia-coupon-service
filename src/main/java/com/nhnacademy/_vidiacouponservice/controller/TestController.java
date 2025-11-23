package com.nhnacademy._vidiacouponservice.controller;

import com.nhnacademy._vidiacouponservice.dto.TestResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public TestResponse getTestString() {
        return new TestResponse("ㅎㅇ?");
    }
}
