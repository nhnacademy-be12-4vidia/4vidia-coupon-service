package com.nhnacademy._vidiacouponservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "4vidia-bookstore-service")
public interface UserClient {

    @GetMapping("/internal/users/{userId}/exists")
    void validateUser(@PathVariable Long userId);
}
