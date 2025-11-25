//package com.dooray.coupon.config;
//
//import com.dooray.coupon.domain.dto.BirthdayResponse;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.List;
//
//@FeignClient(
//    name = "user-service",
//    url = "http://user-service-url" // 나중에 실제 주소/Config Server/Eureka로 교체
//)
//@Component
//@Configuration
//public interface UserClient {
//
//    @GetMapping("/users/birthdays")
//    List<BirthdayResponse> getBirthdayUsers(@RequestParam("month") int month);
//}