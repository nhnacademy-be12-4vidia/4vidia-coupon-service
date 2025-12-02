package com.nhnacademy._vidiacouponservice.domain.dto.response;

import java.time.LocalDate;

public record BirthdayResponse(Long userId, LocalDate birthDate) {}